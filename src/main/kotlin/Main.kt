import elimination.impl.ContinueElimination
import helpers.InputOutput.printList
import helpers.InputOutput.readFileAsLines
import helpers.MethodsLabels
import helpers.Statements
import oldimpl.JumpState
import oldimpl.JumpTransformation
import kotlin.system.measureTimeMillis

fun main() {
    /**
     * берём имя файла
     * сплитим по точке
     * смотрим формат файла после точки -- определяем язык
     * .py
     * .kt
     * .cpp
     * etc
     * */
    val fileName = "C:\\Users\\Mi\\Desktop\\tests\\Test3.kt"
    val list = readFileAsLines(fileName)
    printList(list)

    val cl = ContinueElimination(list, Statements.CONTINUE.operator)
    cl.getTransformedCode()

//    val executionTime = measureTimeMillis {
//        val jumpStates = JumpState(list)
//        val gotoList = jumpStates.getGotoLabelList(Statements.RETURN)
//
//        val jumpTransformation = JumpTransformation(codelines = list, jumpStates = gotoList)
//        jumpTransformation.getTransformedCode()
//    }
//
//    println()
//    print("Execution time: $executionTime ms.")
}
