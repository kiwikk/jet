package helpers

import helpers.InputOutput.printList
import MethodOpenCloseBracket
import helpers.InputOutput.printDivider
import helpers.parsers.NestingHelpers
import language.LanguageHelper.LAN
import language.keywords.KeyWords

object MethodsHelper {
    private val declarationMethodPattern = "\\s\\w*\\(.*\\{$".toRegex()
    private val nameMethodPattern = "\\s\\w*\\(".toRegex()

    private fun getMethodsDeclarationsList(codeLines: List<String>): List<MethodOpenCloseBracket> {
        val methodDeclaration = mutableListOf<MethodOpenCloseBracket>()

        var i = 0
        while (i < codeLines.size) {
            if (declarationMethodPattern.containsMatchIn(codeLines[i]) &&
                LAN.keyWords.all { !codeLines[i].contains(it) }
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

    fun getMethodsWithOperators(codeLines: List<String>, operators: List<String>): List<MethodOpenCloseBracket> {
        val methods = getMethodsDeclarationsList(codeLines)
        val methodsWithOperators = mutableListOf<MethodOpenCloseBracket>()

        for (method in methods) {
            var i = method.startLine
            while (i < method.endLine) {
                if (operators.any { codeLines[i].contains(it) }) {
                    methodsWithOperators.add(method)
                    break
                }
                i++
            }
        }

        return methodsWithOperators
    }

}
