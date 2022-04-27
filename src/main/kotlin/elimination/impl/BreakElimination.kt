package elimination.impl

import BreakTransformedStatement
import CodeToMerge
import elimination.StatementToEliminate
import helpers.RegexHelper
import helpers.parsers.NestingHelpers

class BreakElimination(private val codeLines: List<String>, private val operator: String) :
    StatementToEliminate(operator) {
    var statementList = getEliminatable(codeLines)

    override fun getTransformedCode(): List<String> {
        var result = codeLines

        for (c in statementList) {
            //делаем один - мёрджим делаем один - мёрджим
            val transformation = transform(c.method.startLine, c.method.endLine, result)
            result = merge(transformation, result)
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

    private fun transform(startLine: Int, endLine: Int, codeLines: List<String>): List<CodeToMerge> {
        val breakList = getOperatorList(startLine, endLine, codeLines)
        val transformedBody = mutableListOf<CodeToMerge>()

        for (c in breakList) {
            transformedBody.add(mergeBody(c))
        }

        return transformedBody
    }

    private fun mergeBody(statement: BreakTransformedStatement): CodeToMerge {
        val result = mutableListOf<String>()

        result.add("do {")
        result.add("if(!(${statement.loopCondition})) {")
        result.addAll(statement.body)
        result.add("}")
        result.add("} while (${statement.loopCondition} && !(${statement.innerCondition}))")

        return CodeToMerge(statement.openBodyLine, statement.oldBodyEndLine, result)
    }

    //todo rename
    private fun getOperatorList(
        startLine: Int,
        endLine: Int,
        codeLines: List<String>
    ): List<BreakTransformedStatement> {
        val lines = mutableListOf<BreakTransformedStatement>()
        val nesting = NestingHelpers.getNesting(codeLines, startLine)

        //нужно идти до ближайшего цикла и менять его в do-while
        var i = startLine
        var loopLine = -1
        while (i < endLine) {
            //for, do
            var condition = ""
            if (codeLines[i].contains("while")) {
                loopLine = i
            }
            if (codeLines[i].contains(operator)) {
                //if может быть на несколько строк или { начинаться с новой строки
                var j = loopLine + 1
                val firstNesting = NestingHelpers.getMyNesting(j, nesting)
                val body = mutableListOf<String>()
                while (j < firstNesting.closeNestingLine) {
                    if (codeLines[j].contains(operator)) {
                        j++
                        continue
                    }
                    if ((NestingHelpers.getMyNesting(j + 1, nesting).nesting > firstNesting.nesting)
                        && condition.isEmpty()
                    ) {
                        condition = RegexHelper.getConditionFromStatement(codeLines[j])
                    }
                    body.add(codeLines[j])
                    j++
                }
                body.add(codeLines[j])

                lines.add(
                    BreakTransformedStatement(
                        RegexHelper.getConditionFromStatement(codeLines[loopLine]),
                        condition,
                        body,
                        firstNesting.openNestingLine,
                        j
                    )
                )
                i = j
            }
            i++
        }

        return lines
    }

    fun updateLinks(newCodeLines: List<String>) {
        val tmp = getEliminatable(newCodeLines)

        statementList.forEach { it ->
            tmp.forEach { it1 ->
                if (it.method == it1.method) {
                    it.line = it1.line
                }
            }
        }
    }
}