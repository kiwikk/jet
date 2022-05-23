package helpers

import helpers.InputOutput.printList
import MethodOpenCloseBracket
import OperatorInMethod
import helpers.InputOutput.printDivider
import helpers.InputOutput.printMap
import helpers.parsers.NestingHelpers
import keywords.KeyWords
import transformation.impl.Transformer.Companion.OPERATOR_ID

object MethodsLabels {
    private val methodPattern = "\\s\\w*\\(.*[^{]\$".toRegex()
    private val declarationMethodPattern = "\\s\\w*\\(.*\\{$".toRegex()
    private val nameMethodPattern = "\\s\\w*\\(".toRegex()
    private val signaturePattern = "\\(.*\\)".toRegex()

    fun getMethodsDeclarations(codeLines: List<String>): HashMap<String, Int> {
        val methodDeclaration = hashMapOf<String, Int>()

        codeLines.forEachIndexed { index, s ->
            //аргументы метода могут быть указаны в несколько строк
            //как отличать методы и их вызовы (где-то рядом с методом обычно всегда водится возвращаемый тип) нет, в объявлении есть { в конце
            //for() и прочие ребята задетектятся, надо смотреть, чтобы не было совпадений с кейвордами
            if (declarationMethodPattern.containsMatchIn(s)) {
                val nameFromPattern = nameMethodPattern.find(s)?.value
                if (!nameFromPattern.isNullOrEmpty()) {
                    val name = nameFromPattern.subSequence(1, nameFromPattern.length - 1).toString()
                    methodDeclaration[name] = index
                }
            }
        }

        printDivider()
        println("Methods declarations:")
        printMap(methodDeclaration)
        return methodDeclaration
    }

    fun getMethodsDeclarationsList(codeLines: List<String>): List<MethodOpenCloseBracket> {
        val methodDeclaration = mutableListOf<MethodOpenCloseBracket>()

        var i = 0
        while (i < codeLines.size) {
            if (declarationMethodPattern.containsMatchIn(codeLines[i]) &&
                KeyWords.KotlinKeyWords.all { !codeLines[i].contains(it) }
            ) {
                val nameFromPattern = nameMethodPattern.find(codeLines[i])?.value
                if (!nameFromPattern.isNullOrEmpty()) {
                    val name = nameFromPattern.subSequence(1, nameFromPattern.length - 1).toString()
                    val endLine = NestingHelpers.getNesting(codeLines, i).last().closeNestingLine
                    methodDeclaration.add(MethodOpenCloseBracket(name, i, endLine))
                }
            }
            i++
        }

        printDivider()
        println("Methods declarations:")
        printList(methodDeclaration)
        return methodDeclaration
    }

    fun getMethodsWithOperator(codeLines: List<String>, operator: String): List<MethodOpenCloseBracket> {
        val methods = getMethodsDeclarationsList(codeLines)
        val methodsWithOperators = mutableListOf<MethodOpenCloseBracket>()

        for (method in methods) {
            var i = method.startLine
            while (i < method.endLine) {
                if (codeLines[i].contains(operator)) {
                    methodsWithOperators.add(method)
                    break
                }
                i++
            }
        }

        return methodsWithOperators
    }

    fun getMethodsWithOperators(codeLines: List<String>, operators: List<Statements>): List<MethodOpenCloseBracket> {
        val methods = getMethodsDeclarationsList(codeLines)
        val methodsWithOperators = mutableListOf<MethodOpenCloseBracket>()

        for (method in methods) {
            var i = method.startLine
            while (i < method.endLine) {
                if (operators.any { codeLines[i].contains(it.operatorName) }) {
                    methodsWithOperators.add(method)
                    break
                }
                i++
            }
        }

        return methodsWithOperators
    }

    fun getMethodsCalls(codeLines: List<String>): List<MethodOpenCloseBracket> {
        val methodCalls = mutableListOf<MethodOpenCloseBracket>()

//        codeLines.forEachIndexed { index, s ->
//            if (methodPattern.containsMatchIn(s)) {
//                val nameFromPattern = nameMethodPattern.find(s)?.value
//                val signatureFromPattern
//                if (!nameFromPattern.isNullOrEmpty()) {
//                    val name = nameFromPattern.subSequence(1, nameFromPattern.length - 1).toString()
//                    methodCalls.add(
//                        MethodOpenCloseBracket(
//                            methodName = name,
//                            startLine = index
//                        )
//                    )
//                }
//            }
//        }

        printDivider()
        println("Methods calls:")
        printList(methodCalls)
        return methodCalls
    }
}
