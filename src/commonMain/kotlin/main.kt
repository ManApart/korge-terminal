import com.soywiz.korev.Key
import com.soywiz.korge.Korge
import com.soywiz.korge.annotations.KorgeExperimental
import com.soywiz.korge.input.keys
import com.soywiz.korge.ui.*
import com.soywiz.korge.view.alignBottomToBottomOf
import com.soywiz.korge.view.alignBottomToTopOf
import com.soywiz.korim.color.Colors
import com.soywiz.korim.text.TextAlignment

const val WIDTH = 512.0
const val HEIGHT = 512.0

@KorgeExperimental
suspend fun main() = Korge(width = WIDTH.toInt(), height = HEIGHT.toInt(), bgcolor = Colors["#2b2b2b"]) {
    val completer = StringCompleter("apple", "bob", "candy")
    val processor = ExampleProcessor()

    lateinit var history: UIText
    uiScrollable(width = WIDTH, height = HEIGHT - 40.0, config = {

    }) {
        history = uiText("test\nline2\nline3".repeat(10), WIDTH, HEIGHT - 40.0) {
            textAlignment = TextAlignment.TOP_LEFT
        }
    }

    val suggestions = uiText("", WIDTH) {
        alignBottomToBottomOf(parent!!)
    }

    val prompt = uiTextInput(">", width = WIDTH) {
        alignBottomToTopOf(suggestions)
        focus()
    }

    keys {
        up(Key.TAB) {
            val text = prompt.getText()
            val parts = text.split(" ")
            val options = completer.complete(parts, parts.last())
            if (options.size == 1) {
                val nonReplaced = parts.subList(0, parts.size - 1).joinToString(" ")
                prompt.text = ">$nonReplaced " + options.first() + " "
                prompt.cursorIndex = prompt.text.length
                suggestions.text = ""
            } else {
                suggestions.text = options.joinToString(",")
            }
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