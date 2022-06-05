/**
 * ограничения на данный момент:
 * вложенность должна чётко обозначаться {}
 * то есть у ифов с однострочным телом должны быть {}
 * for пока не обрабатывается из-за нетипичной структуры for в языке Kotlin
 * в дальнейшем будет доработано с заменой на while скорее всего
 * */

import directoryhlpr.DirectoryHelper

fun main(args: Array<String>) {
    val directory = args.lastOrNull()

    try {
        val directoryHelper = DirectoryHelper(directory!!)
        directoryHelper.process()
    }catch (ex: Exception){
        print(ex.message)
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
