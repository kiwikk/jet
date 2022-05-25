/**
 * ограничения на данный момент:
 * вложенность должна чётко обозначаться {}
 * то есть у ифов с однострочным телом должны быть {}
 * for пока не обрабатывается из-за нетипичной структуры for в языке Kotlin
 * в дальнейшем будет доработано с заменой на while скорее всего
 * */

import directoryhlpr.DirectoryHelper

fun main(args: Array<String>) {
    //statistic
    //transformation
    //statistic
    val fileName = "Test4.kt"
    val directory = "C:\\Users\\Mi\\Desktop\\tests2\\"
//    val list = readFileAsLines(directory)
//    printList(list)

    try {
        val directoryHelper = DirectoryHelper(directory)
        directoryHelper.process()
    }catch (ex: Exception){
        print(ex.message)
    }
    //   LanguageHelper.setLanguage(fileName)
//    val codeLines = LanguageHelper.languagePreWork(list)
//
//    val transformer = Transformer(codeLines, Statements.values().toList())
//    var transformed = transformer.transform()
//
//    transformed = LanguageHelper.languagePostWork(transformed)
//
//    val outputDirectory = "C:\\Users\\Mi\\Desktop\\tests\\goto\\output_$fileName"
//    File(outputDirectory).printWriter().use { out ->
//        transformed.forEach {
//            out.println(it)
//        }
//    }

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
