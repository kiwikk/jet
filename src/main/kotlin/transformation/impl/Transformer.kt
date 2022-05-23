package transformation.impl

import CodeToMerge
import transformation.BaseTransformer
import transformation.impl.gototransformation.GotoTransformer
import helpers.Statements
import helpers.StatementsHelper.getStatements

class Transformer(private val codeLines: List<String>, private val operators: List<Statements>) {
    private var statementsLinks = getStatements(codeLines, operators)

    fun transform(): List<String> {
        var result = codeLines

        while (statementsLinks.isNotEmpty()) {
            statementsLinks = getStatements(result, operators)
            for (op in statementsLinks) {
                val transformer: BaseTransformer = when (op.operator) {
                    Statements.GOTO -> GotoTransformer(op)
                    else -> ContinueBreakTransformer(op)
                }

                val transformations = transformer.getTransformedCode(result)
                result = merge(transformations, result)
                updateLinks(result)
            }
        }

        return result
    }

    private fun merge(code: List<CodeToMerge>, lines: List<String>): List<String> {
        val result = lines.toMutableList()

        for (c in code) {
            var i = c.to
            while (i >= c.from) {
                result.removeAt(i--)
            }
            result.addAll(c.from, c.body)
        }

        return result
    }

    private fun updateLinks(newCodeLines: List<String>) {
        val tmp = getStatements(newCodeLines, operators)

        statementsLinks.forEach {
            tmp.forEach { it1 ->
                if (it.id == it1.id
                    && it.method.methodName == it1.method.methodName
                ) {
                    it.line = it1.line
                    it.method = it1.method
                }
            }
        }
    }

    companion object {
        var OPERATOR_ID = 0
    }
}