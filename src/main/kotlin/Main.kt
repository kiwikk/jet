import elimination.impl.ContinueElimination
import helpers.InputOutput.printList
import helpers.InputOutput.readFileAsLines
import helpers.MethodsLabels
import helpers.Statements
import oldimpl.JumpState
import oldimpl.JumpTransformation
import java.io.File
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
    val transformed = cl.getTransformedCode()

    val outputFileName = "C:\\Users\\Mi\\Desktop\\tests\\output_Test3.kt"
    File(outputFileName).printWriter().use { out ->
        transformed.forEach {
            out.println(it)
        }
    }

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
