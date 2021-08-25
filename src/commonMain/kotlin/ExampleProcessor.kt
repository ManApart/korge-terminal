class ExampleProcessor : Processor {
    override fun process(lineIn: String): String {
        return "Processed $lineIn"
    }
}