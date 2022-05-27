package directoryhlpr

import DirectoryException
import statements.Statements
import language.LanguageHelper
import transformation.Statistics
import transformation.impl.Transformer
import java.io.File

class DirectoryHelper(private val directory: String) {
    fun process() {
        val file = File(directory)
        if (file.isDirectory) {
            processDirectory()
        } else if (file.isFile) {
            processFile()
        } else throw DirectoryException()
    }

    private fun processDirectory() {
        File(directory).walk().forEach {
            val fileName = getOutputDirectoryName(it.absolutePath)
            if(it.isFile && !fileName.contains(OUTPUT_FILE_PREFIX)) {
                val lastSeparatorIndex = directory.lastIndexOf(File.separator) + 1
                val outputDirectory = directory.substring(
                    0,
                    lastSeparatorIndex
                ) + "$OUTPUT_DIRECTORY_NAME${File.separator}$OUTPUT_FILE_PREFIX" + fileName
                println("In file: ${getOutputDirectoryName(it.absolutePath)}")
                transform(it, outputDirectory)
            }
        }
    }

    private fun processFile() {
        val lastSeparatorIndex = directory.lastIndexOf(File.separator) + 1
        val outputDirectory = directory.substring(
            0,
            lastSeparatorIndex
        ) + "$OUTPUT_DIRECTORY_NAME${File.separator}$OUTPUT_FILE_PREFIX" + getOutputDirectoryName(directory)
        val file = File(directory)

        transform(file, outputDirectory)
    }

    private fun transform(file: File, outputDirectory: String) {
        File(outputDirectory.substringBeforeLast(File.separator)).mkdirs()

        LanguageHelper.setLanguage(file.absolutePath)
        var codeLines = file.readLines()

        Statistics.getOperatorStatistics(codeLines)
        codeLines = LanguageHelper.languagePreWork(codeLines)

        val transformer = Transformer(codeLines, Statements.values().toList())
        var transformed = transformer.transform()

        transformed = LanguageHelper.languagePostWork(transformed)
        File(outputDirectory).printWriter().use { out ->
            transformed.forEach { line ->
                out.println(line)
            }
        }

        Statistics.getOperatorStatistics(transformed)
        Statistics.getDifference(codeLines, transformed)
    }

    private fun getOutputDirectoryName(directory: String): String {
        val separator = File.separator
        val lastSeparatorIndex = directory.lastIndexOf(separator) + 1
        return directory.substring(lastSeparatorIndex)
    }

    companion object {
        private val OUTPUT_FILE_PREFIX = "Output_"
        private val OUTPUT_DIRECTORY_NAME = "Output"
    }
}