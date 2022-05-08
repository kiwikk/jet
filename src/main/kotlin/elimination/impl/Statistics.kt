package elimination.impl

import OperatorInMethod
import elimination.BaseTransformer
import helpers.Statements

object Statistics {
    fun getOperatorStatistics(operator: Statements, codeLines: List<String>) {
        val operatorsList = mutableListOf<List<OperatorInMethod>>()
        val statementsTransformers = mutableListOf<BaseTransformer>()

        when (operator) {
            Statements.CONTINUE -> ContinueTransformer(operator.operator, codeLines)
            Statements.BREAK -> operatorsList.add(getBreakStatistics(operator, codeLines))
            Statements.GOTO -> operatorsList.add(getGotoStatistics(operator, codeLines))
            else -> print("Operator not under statistics")
        }
    }

    fun getContinueStatistics(operator: Statements, codeLines: List<String>): List<OperatorInMethod> {
        val transformer = ContinueTransformer(operator.operator, codeLines)
        val list = transformer.statementList


    }

    fun getBreakStatistics(operator: Statements, codeLines: List<String>): List<OperatorInMethod> {
        val transformer = BreakTransformer(operator.operator, codeLines)
        val list = transformer.statementList

    }

    fun getGotoStatistics(operator: Statements, codeLines: List<String>): List<OperatorInMethod> {
        val transformer = GotoTransformer(operator.operator, codeLines)
       // val list = transformer.statementList

    }
}