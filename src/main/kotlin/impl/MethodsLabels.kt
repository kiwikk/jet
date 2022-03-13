package impl

import helpers.InputOutput.printList
import MethodLinePair

object MethodsLabels {
    private val methodPattern = "\\(.*\\)".toRegex()
    private val nameMethodPattern = "\\s\\w*\\(".toRegex()

    fun getMethodLables(codeLines: List<String>): List<MethodLinePair> {
        val list = mutableListOf<MethodLinePair>()

        codeLines.forEachIndexed { index, s ->
            //аргументы метода могут быть указаны в несколько строк
            //как отличать методы и их вызовы (где-то рядом с методом обычно всегда водится возвращаемый тип)
            //for() и прочие ребята задетектятся, надо смотреть, чтобы не было совпадений с кейвордами
            if (methodPattern.containsMatchIn(s)) {
                val nameFromPattern = nameMethodPattern.find(s)?.value
                if (!nameFromPattern.isNullOrEmpty()) {
                    val name = nameFromPattern.subSequence(1, nameFromPattern.length - 1).toString()
                    list.add(
                        MethodLinePair(
                            methodName = name,
                            line = index
                        )
                    )
                }
            }
        }

        printList(list)
        print("всё")
        return list
    }
}
