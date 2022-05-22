package elimination

import CodeToMerge
import OperatorInMethod
import MethodOpenCloseBracket
import helpers.MethodsLabels

/**
 * basic functions of each eliminated operator
 * */
abstract class BaseTransformer {
//    open fun getStatements(codeLines: List<String>): List<OperatorInMethod> {
//        val methods = MethodsLabels.getMethodsWithOperator(codeLines, operator)
//        val eliminatable = mutableListOf<OperatorInMethod>()
//
//        for (method in methods) {
//            //строка с оператором, метод
//            val op = getOperatorFromMethod(method, codeLines)
//            eliminatable.addAll(op)
//        }
//
//        return eliminatable
//    }
//
//    fun getOperatorFromMethod(method: MethodOpenCloseBracket, codeLines: List<String>): List<OperatorInMethod> {
//        val op = mutableListOf<OperatorInMethod>()
//        var i = method.startLine
//        while (i < method.endLine) {
//            if ("\\s[^\\w*]$operator".toRegex().containsMatchIn(codeLines[i])) {
//                //op.add(OperatorInMethod(operator, i, method))
//            }
//            i++
//        }
//
//        return op
//    }

    abstract fun getTransformedCode(codeLines: List<String>): List<CodeToMerge>
}