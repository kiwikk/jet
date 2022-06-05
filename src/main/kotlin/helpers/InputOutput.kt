package helpers

import java.io.File

object InputOutput {
    fun readFileAsLines(fileName: String): List<String>
            = File(fileName).readLines()

    fun printList(list: List<Any>) = list.forEach {
        println(it)
    }

    fun printDivider() = println("~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ")
}