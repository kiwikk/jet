package transformation.impl

import CodeToMerge
import GotoInMethod
import Label
import OperatorInMethod
import helpers.StatementsHelper.getLabel
import helpers.StatementsHelper.getLabelNameFromCodeLine
import helpers.parsers.NestingHelpers.getMyNesting
import helpers.parsers.NestingHelpers.getNesting
import language.LanguageHelper.LAN
import transformation.ITransformer
import transformation.impl.gototransformation.GotoType
import transformation.impl.gototransformation.impl.GotoBreak
import transformation.impl.gototransformation.impl.GotoLoop
import transformation.impl.gototransformation.impl.GotoSkip
import transformation.impl.gototransformation.UnknownType

class GotoTransformer(private val operatorInMethod: OperatorInMethod) : ITransformer {
    override fun getTransformedCode(codeLines: List<String>): List<CodeToMerge> {
        val labelLine = findLabel(codeLines)
        val gotoType = getType(codeLines, labelLine.line)

        val gotoStatement = GotoInMethod(
            operatorInMethod.id,
            operatorInMethod.operator,
            operatorInMethod.line,
            labelLine,
            operatorInMethod.method
        )
        val gotoTransformer = when (gotoType) {
            GotoType.LOOP -> GotoLoop(gotoStatement)
            GotoType.SKIP -> GotoSkip(gotoStatement)
            GotoType.BREAK -> GotoBreak(gotoStatement)
            else -> {
                return UnknownType.transform(codeLines, operatorInMethod)
            }
        }

        return gotoTransformer.transform(codeLines)
    }

    fun findLabel(codeLines: List<String>): Label {
        val label = getLabel(codeLines[operatorInMethod.line])
        var i = operatorInMethod.method.startLine
        while (i < operatorInMethod.method.endLine) {
            if (codeLines[i].contains(label) && !codeLines[i].contains(operatorInMethod.operator)) {
                val labelName = getLabelNameFromCodeLine(codeLines[i])
                return Label(labelName, i)
            }
            i++
        }

        return Label("", -1)
    }

    fun getType(codeLines: List<String>, labelLine: Int): GotoType {
        val nesting = getNesting(codeLines, operatorInMethod.method.startLine)
        val labelLineNesting = getMyNesting(labelLine, nesting).nesting

        if (labelLineNesting < getMyNesting(operatorInMethod.line, nesting).nesting
            && (labelLine > operatorInMethod.line)
        ) {
            var i = operatorInMethod.line
            while (i > operatorInMethod.method.startLine
                && getMyNesting(i, nesting).nesting >= labelLineNesting
            ) {
                for (k in LAN.loops) {
                    if (codeLines[i].contains(k.startOfLoop)) {
                        return GotoType.BREAK
                    }
                }
                i--
            }
        }
        if (labelLine < operatorInMethod.line) {
            return GotoType.LOOP
        }
        if ((labelLine > operatorInMethod.line)) {
            return GotoType.SKIP
        }

        return GotoType.UNKNOWN
    }
}