package transformation.impl

import CodeToMerge
import ContinueTransformedStatement
import OperatorInMethod
import transformation.BaseTransformer
import helpers.RegexHelper
import helpers.parsers.NestingHelpers

class ContinueTransformer(val operatorInMethod: OperatorInMethod) : BaseTransformer() {
    override fun getTransformedCode(codeLines: List<String>): List<CodeToMerge> {
        val continueList =
            getStatementBodies(operatorInMethod.method.startLine, operatorInMethod.method.endLine, codeLines)
        val transformedBody = mutableListOf<CodeToMerge>()

        for (c in continueList) {
            transformedBody.add(mergeBody(c))
        }

        return transformedBody
    }

    private fun mergeBody(statement: ContinueTransformedStatement): CodeToMerge {
        val result = mutableListOf<String>()
//        if (statement.conditionBody.size <= 2) {
//            val condition = RegexHelper.getConditionFromStatement(statement.conditionBody.first())
//        } else {
        result.addAll(statement.conditionBody)
        val last = result.removeLast()
        result.add("$last else {")

        statement.afterConditionBody.forEach {
            result.add("\t$it")
        }
        result.add(last)
        //}

        return CodeToMerge(statement.openBodyLine, statement.oldBodyEndLine, result)
    }

    private fun getStatementBodies(
        startLine: Int,
        endLine: Int,
        codeLines: List<String>
    ): List<ContinueTransformedStatement> {
        val lines = mutableListOf<ContinueTransformedStatement>()
        val nesting = NestingHelpers.getNesting(codeLines, startLine)

        var i = startLine
        while (i < endLine) {
            if (RegexHelper.isOperatorInLine(operatorInMethod.operator.operatorName, codeLines[i])) {
                //if может быть на несколько строк или { начинаться с новой строки
                val firstNesting = NestingHelpers.getMyNesting(i, nesting)
                var j = firstNesting.openNestingLine
                val conditionBody = mutableListOf<String>()
                var metOperator = false
                while (j <= firstNesting.closeNestingLine) {
                    if ((codeLines[j].contains(operatorInMethod.operator.operatorName) || metOperator)
                        && !codeLines[j].contains("}")
                    ) {
                        metOperator = true
                        j++
                        continue
                    }
                    conditionBody.add(codeLines[j])
                    j++
                }
                val secondNesting = NestingHelpers.getMyNesting(j, nesting)
                val body = mutableListOf<String>()
                while (j < secondNesting.closeNestingLine) {
                    body.add(codeLines[j])
                    j++
                }
                lines.add(
                    ContinueTransformedStatement(
                        conditionBody,
                        body,
                        firstNesting.openNestingLine,
                        j - 1
                    )
                )
                i = j
            }
            i++
        }

        return lines
    }
}