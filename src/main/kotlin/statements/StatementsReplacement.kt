package statements

enum class StatementsReplacement(val replacement: String) {
    BREAK_REPLACEMENT("break_replacement"),
    CONTINUE_REPLACEMENT("continue_replacement"),
    GOTO_REPLACEMENT("goto_replacement")
}