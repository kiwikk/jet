package transformation.impl

import CodeToMerge
import helpers.ReplacementStatementsHelper
import transformation.ITransformer
import statements.Statements
import helpers.StatementsHelper.getStatements
import transformation.impl.gototransformation.UnknownType

class Transformer(private val codeLines: List<String>, private val operators: List<Statements>) {
    private var statementsLinks = getStatements(codeLines, operators)

    fun transform(): List<String> {
        var result = codeLines

        while (statementsLinks.isNotEmpty()) {
            statementsLinks = getStatements(result, operators)
            for (op in statementsLinks) {
                val transformer: ITransformer = when (op.operator) {
                    Statements.GOTO.operatorName -> GotoTransformer(op)
                    else -> ContinueBreakTransformer(op)
                }

                val transformations = try {
                    transformer.getTransformedCode(result)
                } catch (ex: Exception) {
                    UnknownType.transform(result, op)
                }

                result = merge(transformations, result)
                updateLinks(result)
            }
        }

        val replacements = ReplacementStatementsHelper.getStatements(result)
        for (r in replacements) {
            result = merge(UnknownType.deTransform(result, r), result)
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