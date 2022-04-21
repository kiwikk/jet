package elimination

/**
 * basic functions of each eliminated operator
 * */
interface Eliminatable {
    fun getEliminatable(): List<String>

    fun getTransformedCode(): List<String>
}