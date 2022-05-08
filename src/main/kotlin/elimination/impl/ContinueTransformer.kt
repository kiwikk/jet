package elimination.impl

import CodeToMerge
import ContinueTransformedStatement
import elimination.BaseTransformer
import helpers.RegexHelper
import helpers.parsers.NestingHelpers

class ContinueTransformer(private val operator: String, codeLines: List<String>) :
    BaseTransformer(operator) {
    var statementList = getEliminatable(codeLines)

    override fun getTransformedCode(codeLines: List<String>): List<String> {
        var result = codeLines
        statementList = getEliminatable(codeLines)

        for (c in statementList) {
            //делаем один - мёрджим делаем один - мёрджим
            val transformation = transform(c.method.startLine, c.method.endLine, result)
            result = merge(transformation, result)
            updateLinks(result)
        }

        return result
    }

    private fun merge(code: List<CodeToMerge>, lines: List<String>): List<String> {
        val result = lines.toMutableList()

        for (c in code) {
            var i = c.to
            while (i >= c.from) {
                result.removeAt(i--)
            }
            result.addAll(c.from, c.body)
//
//            val shift = (c.to - c.from)
//            var i = c.to + shift - 1
//            while (i >= c.from + shift) {
//                result.removeAt(i--)
//            }
        }

        return result
    }

    private fun transform(startLine: Int, endLine: Int, codeLines: List<String>): List<CodeToMerge> {
        val continueList = getOperatorList(startLine, endLine, codeLines)
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

    //todo rename
    private fun getOperatorList(
        startLine: Int,
        endLine: Int,
        codeLines: List<String>
    ): List<ContinueTransformedStatement> {
        val lines = mutableListOf<ContinueTransformedStatement>()
        val nesting = NestingHelpers.getNesting(codeLines, startLine)

        var i = startLine
        while (i < endLine) {
            if (RegexHelper.isOperatorInLine(operator, codeLines[i])) {
                //if может быть на несколько строк или { начинаться с новой строки
                val firstNesting = NestingHelpers.getMyNesting(i, nesting)
                var j = firstNesting.openNestingLine
                val conditionBody = mutableListOf<String>()
                var metOperator = false
                while (j <= firstNesting.closeNestingLine) {
                    if ((codeLines[j].contains(operator) || metOperator) && !codeLines[j].contains("}")) {
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

    fun updateLinks(newCodeLines: List<String>) {
        val tmp = getEliminatable(newCodeLines)

        statementList.forEach { it ->
            tmp.forEach { it1 ->
                if (it.method == it1.method) {
                    it.line = it1.line
                }
            }
        }
    }
}