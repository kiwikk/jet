package impl

import helpers.InputOutput.printList
import MethodLinePair
import helpers.InputOutput.printDivider
import helpers.InputOutput.printMap

object MethodsLabels {
    private val methodPattern = "\\s\\w*\\(.*[^{]\$".toRegex()
    private val declarationMethodPattern = "\\s\\w*\\(.*\\{$".toRegex()
    private val nameMethodPattern = "\\s\\w*\\(".toRegex()

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

    fun getMethodsCalls(codeLines: List<String>): List<MethodLinePair> {
        val methodCalls = mutableListOf<MethodLinePair>()

        codeLines.forEachIndexed { index, s ->
            if (methodPattern.containsMatchIn(s)) {
                val nameFromPattern = nameMethodPattern.find(s)?.value
                if (!nameFromPattern.isNullOrEmpty()) {
                    val name = nameFromPattern.subSequence(1, nameFromPattern.length - 1).toString()
                    methodCalls.add(
                        MethodLinePair(
                            methodName = name,
                            line = index
                        )
                    )
                }
            }
        }

        printDivider()
        println("Methods calls:")
        printList(methodCalls)
        return methodCalls
    }
}
