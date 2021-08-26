interface Completer {
    fun complete(lineIn: List<String>, lastWord: String): List<String>
}