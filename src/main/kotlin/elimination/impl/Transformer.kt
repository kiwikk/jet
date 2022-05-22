package elimination.impl

import CodeToMerge
import elimination.BaseTransformer
import helpers.Statements
import helpers.StatementsHelper.getStatements

class Transformer(private val codeLines: List<String>, private val operators: List<Statements>) {
    private val statementsLinks = getStatements(codeLines, operators)

    fun transform(): List<String> {
        var result = codeLines

        for (op in statementsLinks) {
            val transformer: BaseTransformer = when (op.operator) {
                Statements.CONTINUE -> ContinueTransformer(op)
                Statements.BREAK -> BreakTransformer(op)
                Statements.GOTO -> GotoTransformer(op)
            }

            val transformations = transformer.getTransformedCode(result)
            result = merge(transformations, result)
            updateLinks(result)
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
                if (it.method == it1.method) {
                    it.line = it1.line
                }
            }
        }
    }
}