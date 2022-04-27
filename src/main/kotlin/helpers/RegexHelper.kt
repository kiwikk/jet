package helpers

object RegexHelper {
    private val methodPattern = "\\s\\w*\\(.*[^{]\$".toRegex()
    private val declarationMethodPattern = "\\s\\w*\\(.*\\{$".toRegex()
    private val nameMethodPattern = "\\s\\w*\\(".toRegex()
    private val methodArgumentsPattern = "\\(.*\\)".toRegex()
    private val ifConditionPattern = "if\\s*\\(.*\\)".toRegex()
    private val whileConditionPattern = "while\\s*\\(.*\\)".toRegex()

    fun getMetodName(s: String): String? {
        val nameFromPattern = nameMethodPattern.find(s)?.value
        return nameFromPattern?.subSequence(1, nameFromPattern.length - 1)?.toString()
    }

    fun getMethodCalling(s: String): String? {
        val nameFromPattern = methodPattern.find(s)?.value
        return nameFromPattern?.subSequence(1, nameFromPattern.length - 1)?.toString()
    }

    fun getMethodArgumentsNamesFromKotlin(s: String): List<String> {
        val argsFromPattern = methodArgumentsPattern.find(s)?.let {
            val value = it.value
            value.substring(1, value.length - 1)
        }?.split(",")

        val args = mutableListOf<String>()
        if (argsFromPattern != null) {
            for (arg in argsFromPattern) {
                val tmp = arg.let {
                    it.split(":")[0]
                }.filter { !it.isWhitespace() }
                args.add(tmp)
            }
        }

        InputOutput.printDivider()
        println("Arguments names: ")
        InputOutput.printList(args)

        return args
    }

    fun getConditionFromStatement(s: String): String {
        if (!ifConditionPattern.containsMatchIn(s) && !whileConditionPattern.containsMatchIn(s))
            throw IllegalArgumentException()

        val start = s.indexOfFirst { it == '(' } + 1
        val end = s.indexOfLast { it == ')' }

        return s.substring(start, end)
    }

    fun containMethodDeclaration(s: String): Boolean {
        return declarationMethodPattern.containsMatchIn(s)
    }

    fun getMethodArgumentsFromKotlin(s: String): List<String> {
        val argsFromPattern = methodArgumentsPattern.find(s)?.let {
            val value = it.value
            value.substring(1, value.length - 1)
        }?.split(",")

        val args = mutableListOf<String>()
        if (argsFromPattern != null) {
            for (arg in argsFromPattern) {
                args.add(arg.filter { !it.isWhitespace() })
            }
        }

        InputOutput.printDivider()
        println("Arguments in method calling: ")
        InputOutput.printList(args)

        return args
    }
}