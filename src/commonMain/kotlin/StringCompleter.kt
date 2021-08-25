class StringCompleter(private val options: List<String>) : Completer{
    constructor(vararg options: String) : this(options.toList())
    override fun complete(lineIn: String): List<String> {
        return options
            .filter { it.contains(lineIn) }
    }

}