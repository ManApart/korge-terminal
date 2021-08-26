import com.soywiz.korev.Key
import com.soywiz.korev.ReshapeEvent
import com.soywiz.korge.Korge
import com.soywiz.korge.annotations.KorgeExperimental
import com.soywiz.korge.input.keys
import com.soywiz.korge.ui.*
import com.soywiz.korge.view.alignBottomToBottomOf
import com.soywiz.korge.view.alignBottomToTopOf
import com.soywiz.korim.color.Colors
import com.soywiz.korim.text.TextAlignment
import com.soywiz.korma.geom.Anchor
import com.soywiz.korma.geom.ScaleMode

const val WIDTH = 512.0
const val HEIGHT = 512.0
const val MAX_LINES = 26

@KorgeExperimental
suspend fun main() = Korge(
    title = "KorGe Terminal",
    width = WIDTH.toInt(),
    height = HEIGHT.toInt(),
    bgcolor = Colors["#2b2b2b"],
    scaleAnchor = Anchor.TOP_LEFT,
    scaleMode = ScaleMode.SHOW_ALL
) {
    val completer = StringCompleter("apple", "bob", "candy")
    val processor = ExampleProcessor()

    val suggestions = uiText("", WIDTH) {
        alignBottomToBottomOf(parent!!)
    }

    val prompt = uiTextInput("", width = WIDTH) {
        alignBottomToTopOf(suggestions)
        focus()
    }

    val exampleHistory = (0..60).joinToString("\n") { "test $it" }
    val history = uiText(exampleHistory, WIDTH, HEIGHT - 40) {
        textAlignment = TextAlignment.TOP_LEFT
        alignBottomToTopOf(prompt)
    }

    keys {
        up(Key.TAB) {
            val parts = prompt.text.split(" ")
            val options = completer.complete(parts, parts.last())
            if (options.size == 1) {
                val nonReplaced = parts.subList(0, parts.size - 1).joinToString(" ")
                prompt.text = "$nonReplaced " + options.first() + " "
                prompt.cursorIndex = prompt.text.length
                suggestions.text = ""
            } else {
                suggestions.text = options.joinToString(", ")
            }
        }
        up(Key.ENTER) {
            history.text =
                (history.text + "\n" + processor.process(prompt.text)).split("\n").takeLast(MAX_LINES)
                    .joinToString("\n")
            prompt.text = ""
        }
    }

}