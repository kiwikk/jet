package elimination

import OperatorInMethod
import elimination.impl.*
import helpers.Statements
import helpers.StatementsHelper.getStatements

object Statistics {
    fun getOperatorStatistics(codeLines: List<String>) {
        val operatorsList = mutableListOf<OperatorInMethod>()

        for (op in Statements.values()) {
            operatorsList.addAll(getStatisticsByOperator(op, codeLines, true))
        }

        print("Total: ${operatorsList.size}")
    }

    fun getDifference(oldCodeLines: List<String>, newCodeLines: List<String>) {
        val oldOperatorsList = mutableListOf<OperatorInMethod>()
        val newOperatorsList = mutableListOf<OperatorInMethod>()

        for (op in Statements.values()) {
            oldOperatorsList.addAll(getStatisticsByOperator(op, oldCodeLines, false))
            newOperatorsList.addAll(getStatisticsByOperator(op, newCodeLines, true))
        }

        print("Old Total: ${oldOperatorsList.size}")
        print("New Total: ${newOperatorsList.size}")
        val efficiency = oldOperatorsList.size / 100.0 * newOperatorsList.size
        print("Difference Total: ${oldOperatorsList.size - newOperatorsList.size} efficiency: ${"%.2f".format(efficiency)}")
    }

    private fun getStatisticsByOperator(
        operator: Statements,
        codeLines: List<String>,
        output: Boolean
    ): List<OperatorInMethod> {
        val list = getStatements(codeLines, listOf(operator))
        print("${operator.operatorName} was found: ${list.size}")

        if (output) {
            list.forEach {
                print("method: ${it.method.methodName} line: ${it.line}")
            }
        }

        return list
    }
}