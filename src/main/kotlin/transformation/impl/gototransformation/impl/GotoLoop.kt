package transformation.impl.gototransformation.impl

import CodeToMerge
import GotoInMethod
import ProcessedGoto
import helpers.StatementsHelper
import helpers.parsers.NestingHelpers
import language.LanguageHelper
import language.LanguageHelper.LAN
import transformation.impl.gototransformation.BaseGotoTransformer

class GotoLoop(private val statement: GotoInMethod) : BaseGotoTransformer() {
    override fun transform(codeLines: List<String>): List<CodeToMerge> {
        val processedStatement = getProcessedStatement(statement.method.startLine, codeLines)

        return listOf(mergeBody(processedStatement, codeLines))
    }

    private fun mergeBody(processedGoto: ProcessedGoto, codeLines: List<String>): CodeToMerge {
        val result = mutableListOf<String>()
        val nesting = NestingHelpers.getNesting(codeLines, 0)

        var myNesting = NestingHelpers.getMyNesting(processedGoto.lineFrom, nesting)
        val flagName = processedGoto.label.name
        val s = LAN.semicolon
        val newCondition = "${"\t".repeat(myNesting.nesting + 2)}var $flagName = false$s"
        val loopWrapper = listOf(
            "${"\t".repeat(myNesting.nesting + 1)}do {",
            "${"\t".repeat(myNesting.nesting + 1)}} while($flagName)"
        )

        var i = processedGoto.lineFrom + 1
        result.add(loopWrapper[0])
        result.add(newCondition)
        while (i < statement.line) {
            result.add("\t${codeLines[i++]}")
        }
        myNesting = NestingHelpers.getMyNesting(i, nesting)
        val replacement = "${"\t".repeat(myNesting.nesting + 2)}$flagName = true$s"
        result.add(replacement)
        i = myNesting.closeNestingLine
        result.add("${"\t".repeat(myNesting.nesting+1)}}")
        i++
        if (i == processedGoto.lineTo) {
            result.add(codeLines[i])
        }
        while (i < processedGoto.lineTo) {
            myNesting = NestingHelpers.getMyNesting(i, nesting)

            val verification = listOf(
                "${"\t".repeat(myNesting.nesting + 2)}if(!$flagName) {",
                "${"\t".repeat(myNesting.nesting + 2)}}"
            )
            result.add(verification[0])

            var j = i
            while (j < myNesting.closeNestingLine) {
                result.add("\t\t${codeLines[j++]}")
            }
            result.add(verification[1])
            result.add("\t${codeLines[j++]}")
            i = j
        }
        result.add(loopWrapper[1])

        return CodeToMerge(processedGoto.lineFrom, processedGoto.lineTo, result)
    }

    private fun getProcessedStatement(
        startLine: Int,
        codeLines: List<String>
    ): ProcessedGoto {
        val nesting = NestingHelpers.getNesting(codeLines, startLine)

        var i = statement.label.line
        var myNesting = NestingHelpers.getMyNesting(statement.label.line, nesting)
        while (NestingHelpers.getMyNesting(i, nesting).nesting == myNesting.nesting) {
            i++
        }

        myNesting = NestingHelpers.getMyNesting(i, nesting)

        return ProcessedGoto(
            statement.label,
            statement.label.line,
            myNesting.closeNestingLine,
            StatementsHelper.getGotoStatement(codeLines[statement.line])
        )
    }
}