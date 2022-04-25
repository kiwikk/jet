package elimination.impl

import CodeToMerge
import ContinueInMethod
import ContinueTransformedStatement
import MethodOpenCloseBracket
import elimination.Eliminatable
import helpers.MethodsLabels
import helpers.parsers.NestingHelpers

class ContinueElimination(private val codeLines: List<String>, private val operator: String) : Eliminatable {
    //можно заменить ContinueInMethod на что-то более общее (и в родителе тоже)
    override fun getEliminatable(): List<ContinueInMethod> {
        val methods = MethodsLabels.getMethodsWithOperator(codeLines, operator)
        val eliminatable = mutableListOf<ContinueInMethod>()

        for (method in methods) {
            //строка с оператором, метод
            val op = getOperatorFromMethod(method)
            eliminatable.addAll(op)
        }

        return eliminatable
    }

    private fun getOperatorFromMethod(method: MethodOpenCloseBracket): List<ContinueInMethod> {
        val op = mutableListOf<ContinueInMethod>()
        var i = method.startLine
        while (i < method.endLine) {
            if (codeLines[i].contains(operator)) {
                op.add(ContinueInMethod(i, method))
            }
            i++
        }

        return op
    }

    override fun getTransformedCode(): List<String> {
        val methods = MethodsLabels.getMethodsWithOperator(codeLines, operator)
        var result = codeLines
        // val transformedMethods = mutableMapOf<MethodOpenCloseBracket, List<String>>()
        val statementList = getEliminatable()

        for (c in statementList) {
            //делаем один - мёрджим делаем один - мёрджим
            val transformation = transform(c.method.startLine, c.method.endLine, result)
            result = merge(transformation, result)
        }

        return result
    }

    private fun merge(code: List<CodeToMerge>, lines: List<String>): List<String> {
        val result = lines.toMutableList()

        for (c in code) {
            result.addAll(c.from, c.body)

            val shift = (c.to - c.from)
            var i = c.to + shift - 1
            while (i >= c.from + shift) {
                result.removeAt(i--)
            }
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
        result.addAll(statement.conditionBody)
        val last = result.removeLast()
        result.add("$last else {")
        result.addAll(statement.afterConditionBody)
        result.add(last)

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
            if (codeLines[i].contains(operator)) {
                //if может быть на несколько строк или { начинаться с новой строки
                val firstNesting = NestingHelpers.getMyNesting(i, nesting)
                var j = firstNesting.openNestingLine
                val conditionBody = mutableListOf<String>()
                while (j <= firstNesting.closeNestingLine) {
                    if (codeLines[j].contains(operator)) {
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
                        j
                    )
                )
                i = j
            }
            i++
        }

        return lines
    }

    private fun getBody(startLine: Int, endLine: Int): List<String> {
        val body = mutableListOf<String>()
        var i = startLine
        while (i < endLine) {
            body.add(codeLines[i])
            i++
        }

        return body
    }

    private fun wrapToIf(body: List<String>, condition: String): List<String> {
        val wrappedBody = mutableListOf<String>()

        return wrappedBody
    }

//    private fun getAfterOperatorBody(lineFrom: Int, lineTo: Int): List<String> {
//        val body = mutableListOf<String>()
//        var i = lineFrom
//        while (i < lineTo) {
//            body.add(codeLines[i])
//            i++
//        }
//
//        return body
//    }
//
//    private fun getOperatorBody(line: Int): List<String> {
//        val body = mutableListOf<String>()
//        var i = line
//        while (!codeLines[i].contains(operator)) {
//            body.add(codeLines[i])
//            i++
//        }
//
//        return body
//    }
}