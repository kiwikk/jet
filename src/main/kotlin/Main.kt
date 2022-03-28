import helpers.InputOutput.printDivider
import helpers.InputOutput.printList
import helpers.InputOutput.readFileAsLines
import helpers.Statements
import impl.JumpState
import impl.JumpTransformation
import java.sql.Time
import java.time.LocalDateTime
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
    val fileName = "C:\\Users\\Mi\\Desktop\\Test.kt"
    val list = readFileAsLines(fileName)
    printList(list)

    val executionTime = measureTimeMillis {
        val jumpStates = JumpState(list)
        val gotoList = jumpStates.getGotoLabelList(Statements.RETURN)

        val jumpTransformation = JumpTransformation(codelines = list, jumpStates = gotoList)
        jumpTransformation.getTransformedCode()
    }

    println()
    print("Execution time: $executionTime ms.")
}
