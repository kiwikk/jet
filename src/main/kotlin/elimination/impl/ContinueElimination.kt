package elimination.impl

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
        val result = codeLines
        // val transformedMethods = mutableMapOf<MethodOpenCloseBracket, List<String>>()
        val statementList = getEliminatable()

        for (c in statementList) {
            //делаем один - мёрджим делаем один - мёрджим
            val transformation = transform(c.method.startLine, c.method.endLine, result)
        }

//        for (method in methods) {
//            transformedMethods[method] = transform(method.startLine, method.endLine)
//        }
//
//        val result = merge(transformedMethods)

        return result
    }

    private fun merge(map: Map<MethodOpenCloseBracket, List<String>>): List<String> {
        return emptyList()
    }

    private fun transform(startLine: Int, endLine: Int, codeLines: List<String>): List<String> {
        val body = getBody(startLine, endLine)
        val eliminatableList = getOperatorList(startLine, endLine, codeLines)


        return body
    }

    private fun getOperatorList(startLine: Int, endLine: Int, codeLines: List<String>): List<ContinueTransformedStatement> {
        val lines = mutableListOf<ContinueTransformedStatement>()
        val nesting = NestingHelpers.getNesting(codeLines, startLine)

        var i = startLine
        while (i < endLine) {
            if (codeLines[i].contains(operator)) {
                //if может быть на несколько строк или { начинаться с новой строки
                var nearestNesting = NestingHelpers.getMyNesting(i, nesting)
                var j = nearestNesting.openNestingLine
                val conditionBody = mutableListOf<String>()
                while (j <= nearestNesting.nesting) {
                    if (codeLines[i].contains(operator)) {
                        continue
                    }
                    conditionBody.add(codeLines[j])
                    j++
                }
                nearestNesting = NestingHelpers.getMyNesting(i, nesting)
                val body = mutableListOf<String>()
                while (j < nearestNesting.closeNestingLine) {
                    body.add(codeLines[i])
                    j++
                }
                lines.add(
                    ContinueTransformedStatement(
                        conditionBody,
                        body,
                        i + 1
                    )
                )
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