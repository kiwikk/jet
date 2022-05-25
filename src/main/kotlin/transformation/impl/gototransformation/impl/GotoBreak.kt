package transformation.impl.gototransformation.impl

import CodeToMerge
import GotoInMethod
import ProcessedGoto
import helpers.LoopHelper.isLoop
import helpers.StatementsHelper.getGotoStatement
import helpers.parsers.NestingHelpers.getMyNesting
import helpers.parsers.NestingHelpers.getNesting
import language.LanguageHelper.LAN
import transformation.impl.gototransformation.BaseGotoTransformer

class GotoBreak(private val statement: GotoInMethod) : BaseGotoTransformer() {
    override fun transform(codeLines: List<String>): List<CodeToMerge> {
        val processedStatement = getProcessedStatement(statement.method.startLine, codeLines)

        return listOf(mergeBody(processedStatement, codeLines))
    }

    private fun mergeBody(processedGoto: ProcessedGoto, codeLines: List<String>): CodeToMerge {
        val result = mutableListOf<String>()
        val nesting = getNesting(codeLines, 0)

        var myNesting = getMyNesting(processedGoto.lineFrom, nesting)
        val flagName = processedGoto.label.name
        val s = LAN.semicolon
        val newCondition = "${"\t".repeat(myNesting.nesting + 1)}var $flagName = false$s"

        val modulo = listOf(
            "if(!$flagName) {",
            "}"
        )

        var i = processedGoto.lineFrom
        result.add(newCondition)
        while (i < statement.line) {
            result.add(codeLines[i++])
        }
        myNesting = getMyNesting(i, nesting)
        val replacements = listOf(
            "${"\t".repeat(myNesting.nesting + 1)}$flagName = true$s",
            "${"\t".repeat(myNesting.nesting + 1)}break$s"
        )
        result.addAll(replacements)
        i = myNesting.closeNestingLine
        result.add(codeLines[i++])

        if (i == processedGoto.lineTo) {
            result.add(codeLines[i])
        }
        while (i < processedGoto.lineTo) {
            val prevNesting = myNesting
            myNesting = getMyNesting(i, nesting)

            if (isLoop(codeLines, processedGoto.lineFrom, prevNesting)) {
                val verification = listOf(
                    "${"\t".repeat(myNesting.nesting + 1)}if($flagName) {",
                    "${"\t".repeat(myNesting.nesting + 2)}break$s",
                    "${"\t".repeat(myNesting.nesting + 1)}}"
                )
                result.addAll(verification)
            }

            var j = i
            while (j < myNesting.closeNestingLine) {
                result.add(codeLines[j++])
            }
            result.add(codeLines[j++])
            i = j
        }

        val skippedCode = mutableListOf<String>()
        while (i < processedGoto.label.line) {
            skippedCode.add(codeLines[i++])
        }

        if (!(skippedCode.isEmpty()
                    || skippedCode.all { it.all { s -> s.isWhitespace() } })
        ) {
            myNesting = getMyNesting(i, nesting)
            result.add("${"\t".repeat(myNesting.nesting + 1)}${modulo[0]}")
            for (s in skippedCode) {
                result.add("\t$s")
            }
            result.add("${"\t".repeat(myNesting.nesting + 1)}${modulo[1]}")
        }

        return CodeToMerge(processedGoto.lineFrom, processedGoto.label.line, result)
    }

    private fun getProcessedStatement(
        startLine: Int,
        codeLines: List<String>
    ): ProcessedGoto {
        val nesting = getNesting(codeLines, startLine)

        var i = startLine + 1
        var myNesting = getMyNesting(statement.label.line, nesting)
        while (getMyNesting(i, nesting).nesting == myNesting.nesting) {
            i++
        }

        myNesting = getMyNesting(i, nesting)

        return ProcessedGoto(
            statement.label,
            i - 1,
            myNesting.closeNestingLine,
            getGotoStatement(codeLines[statement.line])
        )
    }

}