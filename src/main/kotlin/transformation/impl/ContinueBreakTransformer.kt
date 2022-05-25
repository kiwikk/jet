package transformation.impl

import ProcessedStatement
import CodeToMerge
import OperatorInMethod
import transformation.ITransformer
import helpers.RegexHelper
import statements.Statements
import helpers.StatementsHelper.getFlagName
import helpers.parsers.NestingHelpers.getMyNesting
import helpers.parsers.NestingHelpers.getNesting
import language.LanguageHelper.LAN

class ContinueBreakTransformer(private val operatorInMethod: OperatorInMethod) : ITransformer {
    override fun getTransformedCode(codeLines: List<String>): List<CodeToMerge> {
        val statementBody =
            getStatementBody(operatorInMethod.method.startLine, operatorInMethod.method.endLine, codeLines)

        if (statementBody == null) {
            print("There are no operators in method ${operatorInMethod.method.methodName} on line ${operatorInMethod.line}")
            return emptyList()
        }

        return listOf(mergeBody(statementBody, codeLines))
    }

    private fun mergeBody(statement: ProcessedStatement, codeLines: List<String>): CodeToMerge {
        val result = mutableListOf<String>()
        val nesting = getNesting(codeLines, 0)

        var myNesting = getMyNesting(statement.loopLine - 1, nesting)
        val flagName = getFlagName(operatorInMethod.operator)
        val s = LAN.semicolon
        val newCondition = "${"\t".repeat(myNesting.nesting + 1)}var $flagName = ${statement.loopCondition}$s"
        val newLoop =
            "${"\t".repeat(myNesting.nesting + 1)}while($flagName) {" //todo сделать более гибким для do while, for(?)

        val statementCondition = when (operatorInMethod.operator) {
            Statements.BREAK.operatorName -> "${"\t".repeat(myNesting.nesting + 2)}$flagName = $flagName && (${statement.loopCondition})$s"
            else -> "${"\t".repeat(myNesting.nesting + 2)}$flagName = (${statement.loopCondition})$s"
        }


        var i = statement.loopLine
        result.add(newCondition)
        result.add(newLoop)
        i++
        while (i < statement.statementLine) {
            result.add(codeLines[i++])
        }
        myNesting = getMyNesting(i, nesting)
        val statementReplacement = "${"\t".repeat(myNesting.nesting + 1)}$flagName = false$s"
        result.add(statementReplacement)
        i = myNesting.closeNestingLine
        result.add(codeLines[i++])
        val loopEnd = getMyNesting(statement.loopLine + 1, nesting).closeNestingLine
        if (i == loopEnd) {
            result.add(codeLines[i])
        }
        while (i < loopEnd) {
            myNesting = getMyNesting(i, nesting)

            val statementVerification =
                listOf("${"\t".repeat(myNesting.nesting + 1)}if($flagName) {", "${"\t".repeat(myNesting.nesting + 1)}}")
            result.add(statementVerification[0])

            var j = i
            while (j < myNesting.closeNestingLine) {
                result.add("\t${codeLines[j++]}")
            }
            result.add(statementVerification[1])
            result.add(codeLines[j++])
            i = j
        }

        val tmp = result.removeLast()
        result.add(statementCondition)
        result.add(tmp)

        return CodeToMerge(statement.loopLine, loopEnd, result)
    }


    private fun getStatementBody(
        startLine: Int,
        endLine: Int,
        codeLines: List<String>
    ): ProcessedStatement? {
        val nesting = getNesting(codeLines, startLine)
        var i = startLine
        val loopLine = ArrayDeque<Int>()
        while (i < endLine) {
            for (k in LAN.loops) {
                if (codeLines[i].contains(k.startOfLoop)) {
                    loopLine.add(i)
                }
                if (codeLines[i].contains(k.endOfLoop)) {
                    if (getMyNesting(loopLine.last(), nesting) == getMyNesting(i, nesting) && i != loopLine.last()) {
                        loopLine.removeLast()
                    }
                }
            }
            val regex = "\\s[^\\w*]${operatorInMethod.operator}(;*)\$".toRegex()
            if (regex.containsMatchIn(codeLines[i])) {
                val condition = RegexHelper.getConditionFromStatement(codeLines[loopLine.last()])
                return ProcessedStatement(condition, loopLine.last(), i)
            }
            i++
        }

        return null
    }
}