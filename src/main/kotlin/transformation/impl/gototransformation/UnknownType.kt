package transformation.impl.gototransformation

import CodeToMerge
import OperatorInMethod
import statements.StatementsReplacement.GOTO_REPLACEMENT
import statements.StatementsReplacement.BREAK_REPLACEMENT
import statements.StatementsReplacement.CONTINUE_REPLACEMENT
import statements.Statements.GOTO
import statements.Statements.BREAK
import statements.Statements.CONTINUE


object UnknownType {
    private val map = mapOf(
        GOTO_REPLACEMENT to GOTO,
        BREAK_REPLACEMENT to BREAK,
        CONTINUE_REPLACEMENT to CONTINUE
    )

    fun transform(codeLines: List<String>, statement: OperatorInMethod): List<CodeToMerge> {
        val result = mutableListOf<String>()
        var i = statement.method.startLine
        while (i <= statement.method.endLine) {
            if (i == statement.line) {
                val tmp = when (statement.operator) {
                    GOTO.operatorName -> codeLines[i].replace(statement.operator, GOTO_REPLACEMENT.replacement)
                    CONTINUE.operatorName -> codeLines[i].replace(statement.operator, CONTINUE_REPLACEMENT.replacement)
                    BREAK.operatorName -> codeLines[i].replace(statement.operator, BREAK_REPLACEMENT.replacement)
                    else -> return emptyList()
                }
                result.add(tmp)
                i++
            } else result.add(codeLines[i++])
        }
        return listOf(CodeToMerge(statement.method.startLine, statement.method.endLine, result))
    }

    fun deTransform(codeLines: List<String>, statement: OperatorInMethod): List<CodeToMerge> {
        val result = mutableListOf<String>()
        var i = statement.method.startLine
        while (i <= statement.method.endLine) {
            if (i == statement.line) {
                val tmp = when (statement.operator) {
                    GOTO_REPLACEMENT.replacement -> codeLines[i].replace(
                        GOTO_REPLACEMENT.replacement,
                        map[GOTO_REPLACEMENT]!!.operatorName
                    )
                    CONTINUE_REPLACEMENT.replacement -> codeLines[i].replace(
                        CONTINUE_REPLACEMENT.replacement,
                        map[CONTINUE_REPLACEMENT]!!.operatorName
                    )
                    BREAK_REPLACEMENT.replacement -> codeLines[i].replace(
                        BREAK_REPLACEMENT.replacement,
                        map[BREAK_REPLACEMENT]!!.operatorName
                    )
                    else -> return emptyList()
                }
                result.add(tmp)
                i++
            } else result.add(codeLines[i++])
        }
        return listOf(CodeToMerge(statement.method.startLine, statement.method.endLine, result))
    }
//
//    fun transformGotos(codeLines: List<String>, statement: OperatorInMethod): CodeToMerge {
//        val result = mutableListOf<String>()
//        var i = statement.method.startLine
//        while (i < statement.method.endLine) {
//            if (i == statement.line) {
//                val tmp = (codeLines[i].replace(statement.operator.operatorName, GOTO_REPLACEMENT))
//                result.add(tmp)
//                i++
//            } else
//                result.add(codeLines[i++])
//        }
//
//        return CodeToMerge(statement.method.startLine, statement.method.endLine, result)
//    }
//
//    fun transformOtherStatement(codeLines: List<String>, statement: OperatorInMethod): CodeToMerge {
//        val result = mutableListOf<String>()
//        var i = statement.method.startLine
//        while (i < statement.method.endLine) {
//            if (i == statement.line) {
//                val tmp = when (statement.operator) {
//                    Statements.CONTINUE -> (codeLines[i].replace(
//                        statement.operator.operatorName,
//                        CONTINUE_REPLACEMENT
//                    ))
//                    Statements.BREAK -> (codeLines[i].replace(statement.operator.operatorName, BREAK_REPLACEMENT))
//                    else -> {
//                        i++
//                        continue
//                    }
//                }
//
//                i++
//                result.add(tmp)
//            } else
//                result.add(codeLines[i++])
//        }
//
//        return CodeToMerge(statement.method.startLine, statement.method.endLine, result)
//    }

//    fun deTransformGotos(codeLines: List<String>, statement: GotoInMethod, gotoOriginal: String): CodeToMerge {
//        val result = mutableListOf<String>()
//        var i = statement.method.startLine
//        while (i < statement.method.endLine) {
//            if (codeLines[i].contains(statement.operator.operatorName)) {
//                result.add(gotoOriginal)
//            } else {
//                result.add(codeLines[i++])
//            }
//        }
//
//        return CodeToMerge(statement.method.startLine, statement.method.endLine, result)
//    }
}