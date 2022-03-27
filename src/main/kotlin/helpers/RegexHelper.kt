package helpers

object RegexHelper {
    private val methodPattern = "\\s\\w*\\(.*[^{]\$".toRegex()
    private val declarationMethodPattern = "\\s\\w*\\(.*\\{$".toRegex()
    private val nameMethodPattern = "\\s\\w*\\(".toRegex()
    private val methodArgumentsPattern = "\\(.*\\)".toRegex()

    fun getMetodName(s: String): String? {
        val nameFromPattern = nameMethodPattern.find(s)?.value
        return nameFromPattern?.subSequence(1, nameFromPattern.length - 1)?.toString()
    }

    fun getMethodCalling(s: String): String? {
        val nameFromPattern = methodPattern.find(s)?.value
        return nameFromPattern?.subSequence(1, nameFromPattern.length - 1)?.toString()
    }

    fun getMethodArgumentsNamesFromKotlin(s: String): List<String> {
        val argsFromPattern = methodArgumentsPattern.find(s)?.value?.apply {
            substring(1, length - 2)
        }?.split(",")

        val args = mutableListOf<String>()
        if (argsFromPattern != null) {
            for (arg in argsFromPattern) {
                args.add(arg.split(":")[0])
            }
        }

        InputOutput.printDivider()
        println("Arguments names: ")
        InputOutput.printList(args)

        return args
    }

    fun getMethodArgumentsFromKotlin(s: String): List<String> {
        val argsFromPattern = methodArgumentsPattern.find(s)?.value?.apply {
            substring(1, length - 2)
        }?.split(",")

        val args = mutableListOf<String>()
        if (argsFromPattern != null) {
            for (arg in argsFromPattern) {
                args.add(arg)
            }
        }

        InputOutput.printDivider()
        println("Arguments in method calling: ")
        InputOutput.printList(args)

        return args
    }
}