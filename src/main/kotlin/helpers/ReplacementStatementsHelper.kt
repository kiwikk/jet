package helpers

import MethodOpenCloseBracket
import OperatorInMethod
import language.LanguageHelper
import statements.Statements
import statements.StatementsReplacement
import transformation.impl.Transformer

object ReplacementStatementsHelper {
    fun getStatements(codeLines: List<String>): List<OperatorInMethod> {
        val replacements = StatementsReplacement.values().map { it.replacement }
        val methods = MethodsHelper.getMethodsWithOperators(codeLines, replacements)
        val operatorsInMethod = mutableListOf<OperatorInMethod>()

        for (m in methods) {
            val op = getOperatorsFromMethod(m, codeLines, replacements)
            operatorsInMethod.addAll(op)
        }

        return operatorsInMethod
    }

    fun getOperatorsFromMethod(
        method: MethodOpenCloseBracket,
        codeLines: List<String>,
        operators: List<String>
    ): List<OperatorInMethod> {
        val op = mutableListOf<OperatorInMethod>()
        var i = method.startLine
        while (i < method.endLine) {
            for (operator in operators) {
                var regex = "\\s[^\\w*]${operator}(;*)\$".toRegex()
                if (operator == StatementsReplacement.GOTO_REPLACEMENT.replacement) {
                    regex = "\\s[^\\w*]${operator}".toRegex()
                }
                if (regex.containsMatchIn(codeLines[i])) {
                    op.add(OperatorInMethod(Transformer.OPERATOR_ID++, operator, i, method))
                    continue
                }
            }
            i++
        }
        Transformer.OPERATOR_ID = 0

        return op
    }
}