package transformation.impl.gototransformation

import CodeToMerge
import OperatorInMethod
import helpers.StatementsHelper.getLabel
import helpers.parsers.NestingHelpers.getMyNesting
import helpers.parsers.NestingHelpers.getNesting
import transformation.BaseTransformer

class GotoTransformer(private val operatorInMethod: OperatorInMethod) : BaseTransformer() {
    override fun getTransformedCode(codeLines: List<String>): List<CodeToMerge> {
        var labelLine = findLabel(codeLines) //нам нужно labelName?
        val gotoType = getType(codeLines, labelLine)
        TODO("Not yet implemented")
    }

    private fun findLabel(codeLines: List<String>): Int {
        val label = getLabel(codeLines, operatorInMethod.line)
        var i = operatorInMethod.method.startLine
        while (i < operatorInMethod.method.endLine) {
            if (codeLines[i].contains(label) && !codeLines[i].contains(operatorInMethod.operator.operatorName))
                return i
            i++
        }

        return -1
    }

    private fun getType(codeLines: List<String>, labelLine: Int): GotoType {
        val nesting = getNesting(codeLines, operatorInMethod.method.startLine)
        if (getMyNesting(labelLine, nesting) != getMyNesting(operatorInMethod.line, nesting)
            && labelLine < operatorInMethod.line
        ) {

        }
    }


}