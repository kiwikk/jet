package elimination.impl

import MethodOpenCloseBracket
import elimination.Eliminatable
import helpers.MethodsLabels
import helpers.parsers.NestingHelpers

class ContinueElimination(private val codeLines: List<String>, private val operator: String) : Eliminatable {
    override fun getEliminatable(): List<String> {
//        val methods = MethodsLabels.getMethodsWithOperator(codeLines, operator)
//        val transformedMethods = mutableMapOf<MethodOpenCloseBracket, List<String>>()
//
//        for (method in methods) {
//            transformedMethods[method] = transform(method.startLine, method.endLine)
//        }
//
//        val result = merge(transformedMethods)
//
//        return result
    }

    override fun getTransformedCode(): List<String> {
        val methods = MethodsLabels.getMethodsWithOperator(codeLines, operator)
        val transformedMethods = mutableMapOf<MethodOpenCloseBracket, List<String>>()

        for (method in methods) {
            transformedMethods[method] = transform(method.startLine, method.endLine)
        }

        val result = merge(transformedMethods)

        return result
    }

    private fun merge(map: Map<MethodOpenCloseBracket, List<String>>): List<String> {
        return emptyList()
    }

    private fun transform(startLine: Int, endLine: Int): List<String> {
        val body = getBody(startLine, endLine)
        val eliminatableList = getOperatorList(startLine, endLine)


        return body
    }

    private fun getOperatorList(startLine: Int, endLine: Int): List<String> {
        val lines = mutableListOf<>()
        val nesting = NestingHelpers.getNesting(codeLines, startLine)

        var i = startLine
        while (i < endLine) {
            if (codeLines[i].contains(operator)) {
                //if может быть на несколько строк
                val nearestNesting = NestingHelpers.getNearestNestingBodyBracket(i, nesting)
                var j = nearestNesting.openNestingLine
                val body = mutableListOf<String>()
                while (j < nearestNesting.nesting) {
                    if (codeLines[i].contains(operator)) {
                        continue
                    }
                    body.add(codeLines[j])
                    j++
                }
            }
            i++
        }
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