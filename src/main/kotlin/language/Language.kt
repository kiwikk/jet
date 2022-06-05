package language

import Loop
import language.keywords.KeyWords

sealed class Language(
    val semicolon: String = "",
    val codeStyleNewLine: Boolean = false,
    val loops: List<Loop>,
    val keyWords: List<String> = KeyWords.DEFAULT,
    val restrictKeywords: List<String> = KeyWords.BLACK_LIST_KW
)

class Kotlin :
    Language(
        loops = listOf(Loop("while", "}"), Loop("do {", "while")),
    )

class Java :
    Language(
        semicolon = ";",
        loops = listOf(Loop("while", "}"), Loop("do {", "while")),
        )

class CSharp :
    Language(
        semicolon = ";",
        codeStyleNewLine = true,
        loops = listOf(Loop("while", "}"), Loop("do {", "while")),
    )
