/**
 * ограничения на данный момент:
 * вложенность должна чётко обозначаться {}
 * то есть у ифов с однострочным телом должны быть {}
 * for пока не обрабатывается из-за нетипичной структуры for в языке Kotlin
 * в дальнейшем будет доработано с заменой на while скорее всего
 * */

import elimination.impl.BreakElimination
import elimination.impl.ContinueElimination
import helpers.InputOutput.printList
import helpers.InputOutput.readFileAsLines
import helpers.MethodsLabels
import helpers.RegexHelper
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
    val fileName = "Test2.kt"
    val directory = "C:\\Users\\Mi\\Desktop\\tests\\$fileName"
    val list = readFileAsLines(directory)
    printList(list)

//    val cl = ContinueElimination(list, Statements.CONTINUE.operator)
//    val transformed = cl.getTransformedCode()

//    print(RegexHelper.getConditionFromStatement("if(a==b)"))

    val cl = BreakElimination(list, Statements.BREAK.operator)
    val transformed = cl.getTransformedCode()

    val outputDirectory = "C:\\Users\\Mi\\Desktop\\tests\\output\\output_$fileName"
    File(outputDirectory).printWriter().use { out ->
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
