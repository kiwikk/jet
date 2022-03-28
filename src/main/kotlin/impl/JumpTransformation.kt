package impl

import MethodBody
import OperatorState
import helpers.InputOutput
import helpers.RegexHelper
import helpers.Statements

class JumpTransformation(private val codelines: List<String>, private val jumpStates: List<OperatorState>) {
    private val methodDeclarations = MethodsLabels.getMethodsDeclarations(codelines)
    private val methodInvokations = MethodsLabels.getMethodsCalls(codelines)

    //нужен мёрдж, где метод будем заменять на мапке из <метод, новый вид метода (newCodeLines)>
    fun getTransformedCode(): Map<String, List<String>> {
        val map = mutableMapOf<String, List<String>>()
        for (statement in jumpStates) {
            val invokedMethod = RegexHelper.getMetodName(codelines[statement.inputLine]) ?: "LKSNRGLLDKRGMEPWOKGWL"
            val methodBody = getMethodBody(invokedMethod, statement.operator)
            val methodArguments = getMethodArguments(invokedMethod)
            val argumentsMap = getArgumentsMap(statement.inputLine, methodArguments)
            val transformations = getTransformedStatementPart(argumentsMap, methodBody)
            //вабба лабба даб даб
            //копируем весь метод в котором находимся до входа в нужный нам, вставляем нашу магию и копируем всё, что после
            val newCodeLines =
                getTransformedMethod(
                    statement.callingMethod.invokationLine,
                    statement.inputLine,
                    transformations,
                    methodBody.returnValue!!,
                    invokedMethod
                )

            InputOutput.printDivider()
            InputOutput.printList(newCodeLines)

            map[invokedMethod] = newCodeLines
        }

        return map
    }

    private fun getTransformedMethod(
        start: Int,
        statementInvokedStart: Int,
        transformedMethodBody: List<String>,
        methodReturnedValue: String,
        invokedMethodName: String
    ): List<String> {
        val res = mutableListOf<String>()
        var i = start
        while (!codelines[i].contains("}")) {
            if (i <= statementInvokedStart - 1 || i > statementInvokedStart) {
                res.add(codelines[i])
            } else {
                res.addAll(transformedMethodBody)
            }
            if (codelines[i].contains("=") && codelines[i].contains(invokedMethodName)) {
                val tmp = codelines[i].split("=")[0] + " = $methodReturnedValue"
                res.add(tmp)
            }
            i += transformedMethodBody.size
        }
        res.add(codelines[i])

        return res
    }

    private fun getTransformedStatementPart(
        methodArguments: Map<String, String>,
        methodBody: MethodBody
    ): List<String> {
        val result = mutableListOf<String>()

        methodBody.body.forEachIndexed { index, s ->
            var tmpStr = s
            methodArguments.forEach {
                if (tmpStr.contains(it.key)) {
                    tmpStr = tmpStr.replace(it.key, it.value)
                }
            }
            result.add(index, tmpStr)
        }

        return result
    }

    private fun getArgumentsMap(methodEntry: Int, args: List<String>): Map<String, String> {
        val invokationArgs = RegexHelper.getMethodArgumentsFromKotlin(codelines[methodEntry])
        return args.zip(invokationArgs).toMap()
    }

    //только для котлина )0)
    private fun getMethodArguments(methodEntry: String): List<String> {
        var i = methodDeclarations[methodEntry] ?: throw NoSuchMethodException()
        return RegexHelper.getMethodArgumentsNamesFromKotlin(codelines[i])
    }

    private fun getMethodBody(methodEntry: String, statement: Statements): MethodBody {
        val body = mutableListOf<String>()
        var i = methodDeclarations[methodEntry] ?: throw NoSuchMethodException()

        i++
        while (!codelines[i].contains(statement.operator)) {
            body.add(codelines[i])
            i++
        }

        val returnValue = codelines[i].split(statement.operator)[1]
        return MethodBody(body, returnValue)
    }
}