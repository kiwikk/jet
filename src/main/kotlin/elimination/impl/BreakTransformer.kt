package elimination.impl

import BreakTransformedStatement
import CodeToMerge
import OperatorInMethod
import elimination.BaseTransformer
import helpers.RegexHelper
import helpers.parsers.NestingHelpers.getMyNesting
import helpers.parsers.NestingHelpers.getNesting

class BreakTransformer(private val operatorInMethod: OperatorInMethod) : BaseTransformer() {
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
        val flagName = "breakEliminationFlag"
        val newCondition = "${"\t".repeat(myNesting.nesting + 1)}var $flagName = ${statement.loopCondition}"
        val newLoop =
            "${"\t".repeat(myNesting.nesting + 1)}while($flagName) {" //todo сделать более гибким для do while, for(?)
        val breakCondition =
            "${"\t".repeat(myNesting.nesting + 2)}$flagName = $flagName && (${statement.loopCondition})"

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
        i = getMyNesting(i, nesting).closeNestingLine
        result.add(codeLines[i++])
        val loopEnd = getMyNesting(statement.loopLine + 1, nesting).closeNestingLine
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
        result.add(codeLines[i])

        return CodeToMerge(statement.loopLine, i, result)
    }


    private fun getStatementBody(
        startLine: Int,
        endLine: Int,
        codeLines: List<String>
    ): BreakTransformedStatement? {
        var i = startLine
        var loopLine = -1
        while (i < endLine) {
            if (codeLines[i].contains("while")) {
                loopLine = i
            }
            if (codeLines[i].contains(operatorInMethod.operator.operatorName)) {
                val condition = RegexHelper.getConditionFromStatement(codeLines[loopLine])
                return BreakTransformedStatement(condition, loopLine, i)
            }
            i++
        }

        return null
    }
}