package helpers

import MethodOpenCloseBracket
import OperatorInMethod

object StatementsHelper {
    fun getStatements(codeLines: List<String>, operators: List<Statements>): List<OperatorInMethod> {
        val methods = MethodsLabels.getMethodsWithOperators(codeLines, operators)
        val operatorsInMehod = mutableListOf<OperatorInMethod>()

        for(m in methods){
            val op = getOperatorsFromMethod(m, codeLines, operators)
            operatorsInMehod.addAll(op)
        }

        return operatorsInMehod
    }

    fun getOperatorsFromMethod(
        method: MethodOpenCloseBracket,
        codeLines: List<String>,
        operators: List<Statements>
    ): List<OperatorInMethod> {
        val op = mutableListOf<OperatorInMethod>()
        var i = method.startLine
        while (i < method.endLine) {
            for (operator in operators) {
                if ("\\s[^\\w*]${operator.operatorName}".toRegex().containsMatchIn(codeLines[i])) {
                    op.add(OperatorInMethod(operator, i, method))
                    continue
                }
            }
            i++
        }

        return op
    }
}