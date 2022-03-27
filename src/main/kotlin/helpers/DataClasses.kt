import helpers.Statements

data class LabelNestingPair(val line: Int, val nesting: Int)

data class MethodLinePair(val methodName: String, val line: Int)

data class MethodEnterName(val enterLine: Int, val methodName: String)

data class OperatorState(val className:String, val inputLine: Int, val callingMethod : Method, val operator: Statements)

data class MethodBody(val body: List<String>, val returnValue: String?)

data class Method(val methodName: String, val invokationLine: Int)

