/**
 * ограничения на данный момент:
 * вложенность должна чётко обозначаться {}
 * то есть у ифов с однострочным телом должны быть {}
 * for пока не обрабатывается из-за нетипичной структуры for в языке Kotlin
 * в дальнейшем будет доработано с заменой на while скорее всего
 * */

import transformation.impl.Transformer
import helpers.InputOutput.printList
import helpers.InputOutput.readFileAsLines
import helpers.RegexHelper
import helpers.Statements
import helpers.parsers.NestingHelpers
import java.io.File

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

    //statistic
    //transformation
    //statistic
    val fileName = "Test2.kt"
    val directory = "C:\\Users\\Mi\\Desktop\\tests\\$fileName"
    val list = readFileAsLines(directory)
    printList(list)

//    val nesting = NestingHelpers.getNesting(list, 0)
//    list.forEachIndexed { index, s ->
//        if (index != 0)
//            println("${NestingHelpers.getMyNesting(index, nesting).nesting} $s")
//    }

//    val cl = ContinueElimination(list, Statements.CONTINUE.operator)
//    val transformed = cl.getTransformedCode()

  //  print(RegexHelper.getConditionFromStatement("if(a==b)"))
    val transformer = Transformer(list, Statements.values().toList())
    val transformed = transformer.transform()

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
