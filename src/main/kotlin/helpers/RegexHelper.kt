package helpers

object RegexHelper {
    private val ifConditionPattern = "if\\s*\\(.*\\)".toRegex()
    private val whileConditionPattern = "while\\s*\\(.*\\)".toRegex()

    fun getConditionFromStatement(s: String): String {
        if (!ifConditionPattern.containsMatchIn(s) && !whileConditionPattern.containsMatchIn(s))
            throw IllegalArgumentException()

        val start = s.indexOfFirst { it == '(' } + 1
        val end = s.indexOfLast { it == ')' }

        return s.substring(start, end)
    }
}