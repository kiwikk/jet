package impl

import helpers.InputOutput.printList
import helpers.Statemets

class JumpToLabel(val codeLines: List<String>) {
    private val gotoList = getGotoList(codeLines)
    private val inputPoints = mutableListOf<Int>()

    /**
     * если { - увеличиваем вложенность
     * если метод -- идём в строку метода
     *
     * */
//    fun getGotoLabelList() {
//        var nesting = 0;
//        for (i in codeLines.indices){
//            if(codeLines[i].contains('{'))
//                nesting++
//            if(methodPattern.containsMatchIn(codeLines[i])){
//                inputPoints.add(i-1)
//
//            }
//
//        }
//    }

    private fun getGotoList(list: List<String>): List<Int> {
        val statements = mutableListOf<Int>()
        //from zero
        list.forEachIndexed { index, s ->
            if (s.contains(Statemets.RETURN.operator))
                statements.add(index)
        }
        printList(statements)
        return statements
    }


}