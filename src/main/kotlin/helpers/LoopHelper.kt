package helpers

import LoopException
import OpenClosedNesting
import helpers.parsers.NestingHelpers
import helpers.parsers.NestingHelpers.getMyNesting
import language.LanguageHelper.LAN

object LoopHelper {
    fun isLoop(codeLines: List<String>, fromLine: Int, prevNestingLine:OpenClosedNesting): Boolean {
        //что мы находимся в лупе и что мы закрываем лупу
        val nesting = NestingHelpers.getNesting(codeLines, fromLine)

        for(k in LAN.loops){
            if(codeLines[prevNestingLine.openNestingLine].contains(k.startOfLoop)) {
                return true
            }
        }
        return false



//        var i = myNesting.openNestingLine
//        while (NestingHelpers.getMyNesting(i, nesting).nesting >= myNesting.nesting) {
//            i--
//        }
//
//        for (k in LAN.restrictKeywords) {
//            if(codeLines[i].contains(k)) {
//                throw LoopException()
//            }
//        }
//
//        return false
    }
}