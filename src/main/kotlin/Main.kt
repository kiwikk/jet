import java.io.File

fun main() {
    val fileName = "C:\\Users\\Mi\\Desktop\\Test.kt"
    val list = readFileAsLines(fileName)
    val gotoList = mutableListOf<Int>()
    printList(list)

    //from zero
    list.forEachIndexed { index, s ->
        if(s.contains(Statemets.RETURN.operator))
            gotoList.add(index)
    }

    printList(gotoList)
}

fun readFileAsLines(fileName: String): List<String>
        = File(fileName).readLines()

fun printList(list: List<Any>) = list.forEach {
    println(it)
}