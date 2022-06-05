import statements.Statements

/**
 * @param nesting nesting depth
 * @param line start or end of nesting for this depth
 * */
data class NestingLine(val nesting: Int, val line: Int)

/**
 * @param nesting nesting depth
 * @param openNestingLine start of nesting for this depth
 * @param closeNestingLine end of nesting for this depth
 * */
data class OpenClosedNesting(val nesting: Int, val openNestingLine: Int, val closeNestingLine: Int)

/**
 * @param methodName method name to be linked
 * @param startLine linked start line of the method to be linked
 * @param endLine linked end line of the method to be linked
 * */
data class MethodOpenCloseBracket(val methodName: String, var startLine: Int, var endLine: Int = -1)

/**
 * @param id operator id in method
 * @param operator processed operator
 * @param line operator line
 * @param method method that keeps processed operator
 * */
data class OperatorInMethod(val id: Int, val operator: String, var line: Int, var method: MethodOpenCloseBracket)

/**
 * @param id operator id in method
 * @param operator processed operator
 * @param line operator line
 * @param method method that keeps processed operator
 * @param label goto label (point of going to)
 * */
data class GotoInMethod(
    val id: Int,
    val operator: String,
    var line: Int,
    val label: Label,
    var method: MethodOpenCloseBracket
)

/**
 * @param name label name
 * @param line label line
 * */
data class Label(val name: String, val line: Int)

/**
 * @param loopCondition condition of operator
 * @param loopLine line of operator's loop
 * @param statementLine line of operator
 * */
data class ProcessedStatement(
    val loopCondition: String,
    val loopLine: Int,
    val statementLine: Int,
)

data class ProcessedGoto(
    val label: Label,
    val lineFrom: Int,
    val lineTo: Int,
    val gotoLine: String
)

data class Loop(val startOfLoop: String, val endOfLoop: String)

data class CodeToMerge(val from: Int, val to: Int, val body: List<String>)


