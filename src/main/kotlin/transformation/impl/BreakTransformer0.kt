package transformation.impl

import CodeToMerge
import transformation.BaseTransformer

class BreakTransformer0(private val operator: String, codeLines: List<String>) : BaseTransformer() {
    override fun getTransformedCode(codeLines: List<String>): List<CodeToMerge> {
        TODO("Not yet implemented")
    }
//    var statementList = getStatements(codeLines)
//
//    override fun getTransformedCode(codeLines: List<String>): List<String> {
//        var result = codeLines
//
//        for (c in statementList) {
//            //делаем один - мёрджим делаем один - мёрджим
//            val transformation = transform(c.method.startLine, c.method.endLine, result)
//            result = merge(transformation, result)
//            updateLinks(result)
//        }
//
//        return result
//    }
//
//    private fun merge(code: List<CodeToMerge>, lines: List<String>): List<String> {
//        val result = lines.toMutableList()
//
//        for (c in code) {
//            var i = c.to
//            while (i >= c.from) {
//                result.removeAt(i--)
//            }
//            result.addAll(c.from, c.body)
//        }
//
//        return result
//    }
//
//    private fun transform(startLine: Int, endLine: Int, codeLines: List<String>): List<CodeToMerge> {
//        val breakList = getOperatorList(startLine, endLine, codeLines)
//        val transformedBody = mutableListOf<CodeToMerge>()
//
//        for (c in breakList) {
//            transformedBody.add(mergeBody(c))
//        }
//
//        return transformedBody
//    }
//
//    private fun mergeBody(statement: BreakTransformedStatement): CodeToMerge {
//        val result = mutableListOf<String>()
//
//        result.add("${"\t".repeat(statement.nesting + 1)}do {")
//        result.add("${"\t".repeat(statement.nesting + 2)}if(!(${statement.loopCondition})) {")
//        // result.addAll(statement.body)
//        result.add("${"\t".repeat(statement.nesting + 2)}}")
//        result.add("${"\t".repeat(statement.nesting + 1)}} while (${statement.loopCondition} && !(${statement.innerCondition}))")
//
//        return CodeToMerge(statement.openBodyLine, statement.oldBodyEndLine, result)
//    }
//
//    //todo rename
//    private fun getOperatorList(
//        startLine: Int,
//        endLine: Int,
//        codeLines: List<String>
//    ): List<BreakTransformedStatement> {
//        val lines = mutableListOf<BreakTransformedStatement>()
//        val nesting = NestingHelpers.getNesting(codeLines, startLine)
//
//        //нужно идти до ближайшего цикла и менять его в do-while
//        var i = startLine
//        var loopLine = -1
//        while (i < endLine) {
//            //for, do
//            var condition = ""
//            if (codeLines[i].contains("while")) {
//                loopLine = i
//            }
//            if (codeLines[i].contains(operator)) {
//                //if может быть на несколько строк или { начинаться с новой строки
//                var j = loopLine + 1
//                val firstNesting = NestingHelpers.getMyNesting(j, nesting)
//                val body = mutableListOf<String>()
//                while (j < firstNesting.closeNestingLine) {
//                    if (codeLines[j].contains(operator)) {
//                        j++
//                        continue
//                    }
//                    if ((NestingHelpers.getMyNesting(j + 1, nesting).nesting > firstNesting.nesting)
//                        && condition.isEmpty()
//                    ) {
//                        condition = RegexHelper.getConditionFromStatement(codeLines[j])
//                    }
//                    body.add(codeLines[j])
//                    j++
//                }
//
//                val transformedBody = splitToParts(body.size, j - 1, codeLines, startLine)
//                lines.add(
//                    BreakTransformedStatement(
//                        RegexHelper.getConditionFromStatement(codeLines[loopLine]),
//                        condition,
//                        transformedBody,
//                        firstNesting.nesting,
//                        firstNesting.openNestingLine,
//                        j - 1
//                    )
//                )
//                i = j
//            }
//            i++
//        }
//
//        return lines
//    }
//
//    //todo разбить на 4 метода
//    private fun splitToParts(oldBodySize: Int, line: Int, codeLines: List<String>, startLine: Int): BreakBody {
//        var body: BreakBody? = null
//        val nesting = NestingHelpers.getNesting(codeLines, startLine)
//
//        var i = line - oldBodySize
//        while (i <= line) {
//            if (codeLines[i].contains(operator)) {
//                val firstNesting = NestingHelpers.getMyNesting(i, nesting)
//                var j = firstNesting.openNestingLine + 1
//                val conditionBody = mutableListOf<String>()
//                var metOperator = false
//                while (j < firstNesting.closeNestingLine) {
//                    if ((codeLines[j].contains(operator) || metOperator) && !codeLines[j].contains("}")) {
//                        metOperator = true
//                        j++
//                        continue
//                    }
//                    conditionBody.add(codeLines[j])
//                    j++
//                }
//                j++
//
//                var secondNesting = NestingHelpers.getMyNesting(j, nesting)
//                val afterConditionBody = mutableListOf<String>()
//                while (j < secondNesting.closeNestingLine) {
//                    afterConditionBody.add(codeLines[j])
//                    j++
//                }
//                val beforeOperatorBody = mutableListOf<String>()
//                j = secondNesting.openNestingLine + 1
//                while (j < firstNesting.openNestingLine) {
//                    beforeOperatorBody.add(codeLines[j])
//                    j++
//                }
//
//                val loopRemainder = mutableListOf<String>()
//                j = secondNesting.closeNestingLine + 1
//                val thirdNesting = NestingHelpers.getMyNesting(j, nesting)
//                while (j < thirdNesting.closeNestingLine) {
//                    loopRemainder.add(codeLines[j])
//                    j++
//                }
//
//                body = BreakBody(
//                    beforeOperatorBody,
//                    conditionBody,
//                    afterConditionBody,
//                    loopRemainder
//                )
//                i = j
//            }
//            i++
//        }
//        if (body == null) throw IllegalArgumentException()
//
//        return body
//    }
//
//    fun updateLinks(newCodeLines: List<String>) {
//        val tmp = getStatements(newCodeLines)
//
//        statementList.forEach { it ->
//            tmp.forEach { it1 ->
//                if (it.method == it1.method) {
//                    it.line = it1.line
//                }
//            }
//        }
//    }
}