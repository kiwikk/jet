import helpers.InputOutput.printList
import helpers.InputOutput.readFileAsLines
import impl.MethodsLabels

fun main() {
    val fileName = "C:\\Users\\Mi\\Desktop\\Test.kt"
    val list = readFileAsLines(fileName)
    printList(list)

    val a = MethodsLabels.getMethodLables(list)
    //val jumpToLabel = impl.JumpToLabel(list)
}
