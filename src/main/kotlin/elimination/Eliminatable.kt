package elimination

/**
 * basic functions of each eliminated operator
 * */
interface Eliminatable {
    fun getEliminatable(): List<Any>

    fun getTransformedCode(): List<String>
}