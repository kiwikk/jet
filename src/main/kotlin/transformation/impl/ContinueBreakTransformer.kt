package transformation.impl

import BreakTransformedStatement
import CodeToMerge
import NestingLine
import OperatorInMethod
import transformation.BaseTransformer
import helpers.RegexHelper
import helpers.Statements
import helpers.StatementsHelper.getFlagName
import helpers.parsers.NestingHelpers.getMyNesting
import helpers.parsers.NestingHelpers.getNesting

class ContinueBreakTransformer(private val operatorInMethod: OperatorInMethod) : BaseTransformer() {
    override fun getTransformedCode(codeLines: List<String>): List<CodeToMerge> {
        val breakBody =
            getStatementBody(operatorInMethod.method.startLine, operatorInMethod.method.endLine, codeLines)

        if (breakBody == null) {
            print("There are no operators in method ${operatorInMethod.method.methodName} on line ${operatorInMethod.line}")
            return emptyList()
        }

        return listOf(mergeBody(breakBody, codeLines))
    }

    private fun mergeBody(statement: BreakTransformedStatement, codeLines: List<String>): CodeToMerge {
        val result = mutableListOf<String>()
        val nesting = getNesting(codeLines, 0)

        var myNesting = getMyNesting(statement.loopLine - 1, nesting)
        val flagName = getFlagName(operatorInMethod.operator)
        val newCondition = "${"\t".repeat(myNesting.nesting + 1)}var $flagName = ${statement.loopCondition}"
        val newLoop =
            "${"\t".repeat(myNesting.nesting + 1)}while($flagName) {" //todo сделать более гибким для do while, for(?)

        val breakCondition = when (operatorInMethod.operator) {
            Statements.BREAK -> "${"\t".repeat(myNesting.nesting + 2)}$flagName = $flagName && (${statement.loopCondition})"
            else -> "${"\t".repeat(myNesting.nesting + 2)}$flagName = (${statement.loopCondition})"
        }


        var i = statement.loopLine
        result.add(newCondition)
        result.add(newLoop)
        i++
        while (i < statement.breakLine) {
            result.add(codeLines[i++])
        }
        myNesting = getMyNesting(i, nesting)
        val breakReplacement = "${"\t".repeat(myNesting.nesting + 1)}$flagName = false"
        result.add(breakReplacement)
        i = myNesting.closeNestingLine
        result.add(codeLines[i++])
        val loopEnd = getMyNesting(statement.loopLine + 1, nesting).closeNestingLine
        if (i == loopEnd) {
            result.add(codeLines[i])
        }
        while (i < loopEnd) {
            myNesting = getMyNesting(i, nesting)

            val breakVerification =
                listOf("${"\t".repeat(myNesting.nesting + 1)}if($flagName) {", "${"\t".repeat(myNesting.nesting + 1)}}")
            result.add(breakVerification[0])

            var j = i
            while (j < myNesting.closeNestingLine) {
                result.add("\t${codeLines[j++]}")
            }
            result.add(breakVerification[1])
            result.add(codeLines[j++])
            i = j
        }

        val tmp = result.removeLast()
        result.add(breakCondition)
        result.add(tmp)

        return CodeToMerge(statement.loopLine, loopEnd, result)
    }


    private fun getStatementBody(
        startLine: Int,
        endLine: Int,
        codeLines: List<String>
    ): BreakTransformedStatement? {
        val nesting = getNesting(codeLines, startLine)
        var i = startLine
        val loopLine = ArrayDeque<Int>()
        while (i < endLine) {
            if (codeLines[i].contains("while")) {
                loopLine.add(i)
            }
            if (codeLines[i].contains("}")) {
                if(getMyNesting(loopLine.last(), nesting) == getMyNesting(i, nesting)) {
                    loopLine.removeLast()
                }
            }
            val regex = "\\s[^\\w*]${operatorInMethod.operator.operatorName}(;*)\$".toRegex()
            if (regex.containsMatchIn(codeLines[i])) {
                val condition = RegexHelper.getConditionFromStatement(codeLines[loopLine.last()])
                return BreakTransformedStatement(condition, loopLine.last(), i)
            }
            i++
        }

        return null
    }
}