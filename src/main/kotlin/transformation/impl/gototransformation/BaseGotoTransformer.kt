package transformation.impl.gototransformation

import CodeToMerge
import helpers.parsers.NestingHelpers.getMyNesting
import helpers.parsers.NestingHelpers.getNesting

abstract class BaseGotoTransformer {
    abstract fun transform(codeLines: List<String>): List<CodeToMerge>

//    fun isLoop(codeLines: List<String>, line: Int, fromLine: Int): Boolean {
//        val nesting = getNesting(codeLines, fromLine)
//        val myNesting = getMyNesting(line, nesting)
//
//        var i = myNesting.openNestingLine
//        while (getMyNesting(i, nesting).nesting == myNesting.nesting) {
//            i--
//        }
//
//        //todo loop
//        if (codeLines[i].contains("while")) {
//            return true
//        }
//
//        return false
//    }
}