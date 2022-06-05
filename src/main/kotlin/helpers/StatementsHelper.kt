package helpers

import MethodOpenCloseBracket
import OperatorInMethod
import language.LanguageHelper.LAN
import statements.Statements
import transformation.impl.Transformer.Companion.OPERATOR_ID

object StatementsHelper {
    private val gotoLabelPattern = "goto\\s*\\w*".toRegex()
    private val labelPattern = "^\\s*\\w*[^;{}()]".toRegex()

    fun getStatements(codeLines: List<String>, operators: List<Statements>): List<OperatorInMethod> {
        val methods = MethodsHelper.getMethodsWithOperators(codeLines, operators.map { it.operatorName })
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
                var regex = "\\s[^\\w*]${operator.operatorName}(;*)\$".toRegex()
                if (operator == Statements.GOTO) {
                    regex = "\\s[^\\w*]${operator.operatorName}".toRegex()
                }
                if (regex.containsMatchIn(codeLines[i])) {
                    op.add(OperatorInMethod(OPERATOR_ID++, operator.operatorName, i, method))
                    continue
                }
            }
            i++
        }
        OPERATOR_ID = 0

        return op
    }

    fun getLabel(line: String): String {
        return if (line.contains(gotoLabelPattern)) {
            val i = line.indexOfFirst { it.isLetter() }
            line.substring(i).split(" ")[1].apply {
                labelPattern.find(this)?.value
            }.replace(LAN.semicolon, "")

        } else ""
    }

    fun getGotoStatement(line: String): String {
        val i = line.indexOfFirst { it.isLetter() }
        return line.substring(i)
    }

    fun getFlagName(statement: String): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return statement + "_" + (1..10)
            .map { allowedChars.random() }
            .joinToString("")
    }

    fun getLabelNameFromCodeLine(codeLine: String): String {
        return codeLine.filter { it.isLetter() }.replace(LAN.semicolon, "")
    }
}