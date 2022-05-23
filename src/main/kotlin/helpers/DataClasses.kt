import helpers.Statements


data class OperatorState(val className: String, val inputLine: Int, val callingMethod: Method, val operator: Statements)

data class MethodBody(val body: List<String>, val returnValue: String?)

data class Method(val methodName: String, val invokationLine: Int)

data class Elimination(val operator: Statements, val line: Int, val nesting: Int, val unwrappingLine: Int)

data class Conditional(val conditional: String)

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

data class OperatorInMethod(val id: Int, val operator: Statements, var line: Int, var method: MethodOpenCloseBracket)

data class ContinueTransformedStatement(
    val conditionBody: List<String>,
    val afterConditionBody: List<String>,
    val openBodyLine: Int,
    val oldBodyEndLine: Int = -1
)

//data class BreakTransformedStatement(
//    val loopCondition: String,
//    val innerCondition: String,
//    val body: BreakBody,
//    val nesting: Int,
//    val openBodyLine: Int,
//    val oldBodyEndLine: Int = -1
//)

data class BreakTransformedStatement(
    val loopCondition: String,
    val loopLine: Int,
    val breakLine: Int,
)

data class BreakBody(
    val beforeOperatorBody: List<String>,
    val conditionBody: List<String>,
    val afterConditionBody: List<String>,
    val loopRemainder: List<String>
)

data class CodeToMerge(val from: Int, val to: Int, val body: List<String>)

data class BodyLine(val line: Int, val body: List<String>)


