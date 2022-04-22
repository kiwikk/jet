package helpers.parsers

import NestingLine
import OpenClosedNesting

object NestingHelpers {
    /**
     * @param codeLines the code lines whose nesting you would like to know
     *
     * @return list with nestings and it's opening and closing lines
     * */
    fun getNesting(codeLines: List<String>, startLine:Int): List<OpenClosedNesting> {
        val stack = ArrayDeque<NestingLine>()
        val nestingList = mutableListOf<OpenClosedNesting>()

        var i = startLine
        var depth = 0
        do {
            if (codeLines[i].contains("{")) {
                val nesting = NestingLine(depth++, i)
                stack.addLast(nesting)
            }
            if (codeLines[i].contains("}")) {
                val nesting = stack.removeLast()
                nestingList.add(OpenClosedNesting(nesting.nesting, nesting.line, i))
                depth--
            }
            i++
        } while (!stack.isEmpty())

        return nestingList
    }

    /**
     * @param line the code line for which you want to determine the depth
     * @param openClosedNestingList list of nesting bodies
     *
     * @return nesting of input line
     * */
    fun getMyNesting(line: Int, openClosedNestingList: List<OpenClosedNesting>): OpenClosedNesting {
        val openBrackets = openClosedNestingList.filter { it.closeNestingLine > line }
        val tmp = openBrackets.map { it to ((line - it.openNestingLine) + (it.closeNestingLine - line))/2 }
        val res = tmp.minByOrNull { it.second }
        return res!!.first
    }
}