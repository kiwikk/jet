package language

import LanguageException
import helpers.parsers.NestingHelpers.getMyNesting
import helpers.parsers.NestingHelpers.getNesting

object LanguageHelper {
    var LAN: Language = Kotlin()

    fun setLanguage(fileName: String) {
        LAN = when (fileName.substringAfterLast('.')) {
            "kt" -> Kotlin()
            "cs" -> CSharp()
            "java" -> Java()
            "" -> LAN
            else -> throw LanguageException()
        }
    }

    fun languagePreWork(codeLines: List<String>): List<String> {
        val result = mutableListOf<String>()
        if (LAN.codeStyleNewLine) {
            var i = 0
            while (i < codeLines.size) {
                if (codeLines[i].contains("{")) {
                    val tmp =
                        result.removeLast() + " " + codeLines[i].substring(codeLines[i].indexOfFirst { it.isLetter() })
                    result.add(tmp)
                    i++
                } else {
                    result.add(codeLines[i++])
                }
            }
        }

        return if (result.isEmpty()) codeLines else result
    }

    fun languagePostWork(codeLines: List<String>): List<String> {
        val result = mutableListOf<String>()
        if (LAN.codeStyleNewLine) {
            val nesting = getNesting(codeLines, 0)
            var i = 0
            while (i < codeLines.size) {
                if (codeLines[i].contains("{")) {
                    val index = codeLines[i].indexOfFirst { it == '{' }
                    val tmp = codeLines[i].substring(0, index) + "\n${"\t".repeat(getMyNesting(i, nesting).nesting)}" +
                            codeLines[i].substring(index)
                    result.add(tmp)
                } else {
                    result.add(codeLines[i++])
                }
            }
        }

        return if (result.isEmpty()) codeLines else result
    }
}