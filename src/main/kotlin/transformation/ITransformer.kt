package transformation

import CodeToMerge

/**
 * basic functions of each eliminated operator
 * */
interface ITransformer {
    fun getTransformedCode(codeLines: List<String>): List<CodeToMerge>
}