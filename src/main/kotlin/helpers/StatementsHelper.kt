package helpers

import MethodOpenCloseBracket
import OperatorInMethod
import transformation.impl.Transformer.Companion.OPERATOR_ID

object StatementsHelper {
    private val gotoLabelPattern = "goto\\s*\\w*".toRegex()
    private val labelPattern = "^\\s*\\w*[^;{}()]".toRegex()

    fun getStatements(codeLines: List<String>, operators: List<Statements>): List<OperatorInMethod> {
        val methods = MethodsLabels.getMethodsWithOperators(codeLines, operators)
        val operatorsInMethod = mutableListOf<OperatorInMethod>()

        for (m in methods) {
            val op = getOperatorsFromMethod(m, codeLines, operators)
            operatorsInMethod.addAll(op)
        }

        return operatorsInMethod
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
                val regex = "\\s[^\\w*]${operator.operatorName}(;*)\$".toRegex()
                if (regex.containsMatchIn(codeLines[i])) {
                    op.add(OperatorInMethod(OPERATOR_ID++, operator, i, method))
                    continue
                }
            }
            i++
        }
        OPERATOR_ID = 0

        return op
    }

    fun getLabel(codeLines: List<String>, operatorLine: Int): String {
        return if(codeLines[operatorLine].contains(gotoLabelPattern)){
            codeLines[operatorLine].split(" ")[1].apply {
                labelPattern.find(this)?.value
            }

        } else ""
    }

    fun getFlagName(statement: Statements): String{
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return statement.operatorName + "_" + (1..10)
            .map { allowedChars.random() }
            .joinToString("")
    }
}