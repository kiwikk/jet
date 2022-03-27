package impl

import Method
import OperatorState
import helpers.InputOutput.printDivider
import helpers.InputOutput.printList
import helpers.RegexHelper
import helpers.Statements

class JumpState(val codeLines: List<String>) {
    private val gotoList = getGotoList(codeLines)
    private val inputPoints = ArrayDeque<Int>()
    private val methodPattern = "\\s\\w*\\(.*[^{]\$".toRegex()
    private val classPattern = "\\w*\\bclass\\s\\w*".toRegex()
    private val methodsDeclarations = MethodsLabels.getMethodsDeclarations(codeLines)
    private val methodsCalls = MethodsLabels.getMethodsCalls(codeLines)


    /**
     * если { - увеличиваем вложенность
     * если метод -- идём в строку метода
     * запоминаем откуда пошли в строку метода
     * я забыла зачем нужна вложенность (увеличивать вложенность при входе в метод если вспомню зачем она нужна)
     * if(nesting != 0) то мы возвращаем return на предущую вложенность и так пока она не станет равной нулю
     *
     * */
    fun getGotoLabelList(statement: Statements) : List<OperatorState> {
        var nesting = 0
        var i = -1
       // val enterPointStack = ArrayDeque<Int>()
        var currentClassName = ""
        var currentMethod = Method("VabbaLubbaDubDub", -1)
       // val enterList = mutableListOf<Int>()
        val operatorStatesList = mutableListOf<OperatorState>()
        while (i < codeLines.size-1) {
            i++
            if (classPattern.containsMatchIn(codeLines[i])) {
                val classNameFromPattern = classPattern.find(codeLines[i])?.value
                if (!classNameFromPattern.isNullOrEmpty())
                    currentClassName = classNameFromPattern.split(" ")[1]
            }
            if (codeLines[i].contains('{')) {
               // enterPointStack.add(i)
                nesting++
            }
            if (methodPattern.containsMatchIn(codeLines[i])) {
                inputPoints.add(i)
                currentMethod = Method(RegexHelper.getMetodName(codeLines[i]) ?: continue, i)
                val methodLine = methodsDeclarations[currentMethod.methodName] ?: continue
                //входим в метод (котлиновские методы могут быть записаны в одну строку)
                i = methodLine - 1
                // добавляем строку входа и вложенность на которой она находится, чтобы потом по ней разворачивать ретёрн
                // надо ли тогда запоминать на какой строке лежит ретёрн? скорее да чем нет
            }
            if (codeLines[i].contains(statement.operator)) {
                operatorStatesList.add(
                    OperatorState(
                        className = currentClassName,
                        inputLine = inputPoints.removeLast(),
                        callingMethod = currentMethod,
                        operator = statement
                    )
                )
            }
            if (codeLines[i].contains('}')) {
               // i = enterPointStack.removeLast()
            }
        }

        return operatorStatesList
    }

    private fun getGotoList(list: List<String>): List<Int> {
        val statements = mutableListOf<Int>()
        //from zero
        list.forEachIndexed { index, s ->
            if (s.contains(Statements.RETURN.operator))
                statements.add(index)
        }
        printDivider()
        println("Operator ${Statements.RETURN.operator} was found at the lines:")
        printList(statements)
        return statements
    }


}