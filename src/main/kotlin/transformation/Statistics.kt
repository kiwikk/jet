package transformation

import OperatorInMethod
import statements.Statements
import helpers.StatementsHelper.getStatements

object Statistics {
    fun getOperatorStatistics(codeLines: List<String>) {
        println(" - - - - - - - Statistics Start - - - - - - - - -")
        val operatorsList = mutableListOf<OperatorInMethod>()

        for (op in Statements.values()) {
            operatorsList.addAll(getStatisticsByOperator(op, codeLines, true))
        }


        println("Total: ${operatorsList.size}")
        println(" - - - - - - - Statistics End - - - - - - - - -")
    }

    fun getDifference(oldCodeLines: List<String>, newCodeLines: List<String>) {
        println(" - - - - - - - Difference Start - - - - - - - - -")
        val oldOperatorsList = mutableListOf<OperatorInMethod>()
        val newOperatorsList = mutableListOf<OperatorInMethod>()

        for (op in Statements.values()) {
            oldOperatorsList.addAll(getStatisticsByOperator(op, oldCodeLines, false))
            newOperatorsList.addAll(getStatisticsByOperator(op, newCodeLines, true))
        }

        println("Old Total: ${oldOperatorsList.size}")
        println("New Total: ${newOperatorsList.size}")
        val efficiency = 100 - (newOperatorsList.size / oldOperatorsList.size) * 100.0
        println("Difference Total: ${oldOperatorsList.size - newOperatorsList.size} efficiency: ${"%.2f".format(efficiency)}")
        println(" - - - - - - - Difference End - - - - - - - - -")
    }

    private fun getStatisticsByOperator(
        operator: Statements,
        codeLines: List<String>,
        output: Boolean
    ): List<OperatorInMethod> {
        val list = getStatements(codeLines, listOf(operator))
        println("${operator.operatorName} was found: ${list.size}")

        if (output) {
            list.forEach {
                println("method: ${it.method.methodName} line: ${it.line}")
            }
        }

        return list
    }
}