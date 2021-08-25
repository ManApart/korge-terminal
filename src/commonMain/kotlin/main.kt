import com.soywiz.korev.Key
import com.soywiz.korge.Korge
import com.soywiz.korge.annotations.KorgeExperimental
import com.soywiz.korge.input.keys
import com.soywiz.korge.ui.*
import com.soywiz.korge.view.alignBottomToBottomOf
import com.soywiz.korim.color.Colors

const val WIDTH = 512.0
const val HEIGHT = 512.0

@KorgeExperimental
suspend fun main() = Korge(width = WIDTH.toInt(), height = HEIGHT.toInt(), bgcolor = Colors["#2b2b2b"]) {
    val completer = StringCompleter("apple", "bob", "candy")
    val processor = ExampleProcessor()

    lateinit var history: UIText
    uiScrollable(width = WIDTH, height = HEIGHT - 40.0) {
        history = uiText("", WIDTH, HEIGHT - 40.0)
    }

    val prompt = uiTextInput(">", width = WIDTH) {
        alignBottomToBottomOf(parent!!)
        focus()
    }


    keys {
        up(Key.TAB) {
            val text = prompt.getText()
            val options = completer.complete(text)
            println("Options: ${options.joinToString(",")}")
        }
        up(Key.ENTER) {
            val text = prompt.getText()
            history.text += processor.process(text) + "\n"
            prompt.text = ">"
        }
    }

}

@KorgeExperimental
private fun UITextInput.getText(): String {
    return if (text.startsWith(">")) {
        text.substring(1)
    } else text
}