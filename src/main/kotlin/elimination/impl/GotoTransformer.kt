package elimination.impl

import CodeToMerge
import OperatorInMethod
import elimination.BaseTransformer

class GotoTransformer(private val operatorInMethod: OperatorInMethod) : BaseTransformer() {
    override fun getTransformedCode(codeLines: List<String>): List<CodeToMerge> {
        TODO("Not yet implemented")
    }
}