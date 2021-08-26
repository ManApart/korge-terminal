import com.soywiz.korev.Key
import com.soywiz.korge.Korge
import com.soywiz.korge.annotations.KorgeExperimental
import com.soywiz.korge.input.keys
import com.soywiz.korge.ui.*
import com.soywiz.korge.view.alignBottomToBottomOf
import com.soywiz.korge.view.alignBottomToTopOf
import com.soywiz.korge.view.positionY
import com.soywiz.korim.color.Colors
import com.soywiz.korim.text.TextAlignment

const val WIDTH = 512.0
const val HEIGHT = 512.0
const val LINE_HEIGHT = 15.0

@KorgeExperimental
suspend fun main() = Korge(width = WIDTH.toInt(), height = HEIGHT.toInt(), bgcolor = Colors["#2b2b2b"]) {
    val completer = StringCompleter("apple", "bob", "candy")
    val processor = ExampleProcessor()
//    val history = (0..20).map { "test $it" }.toMutableList()
    lateinit var history: UIText

//    val stack = uiVerticalStack(WIDTH) {
//        history.forEach {
//            uiText(it, WIDTH, 20.0)
//        }
//    }

    val suggestions = uiText("", WIDTH) {
        alignBottomToBottomOf(parent!!)
    }

    val prompt = uiTextInput("", width = WIDTH) {
        alignBottomToTopOf(suggestions)
        focus()
    }

//    uiScrollable(width = WIDTH, height = HEIGHT - 40.0) {
        val text = (0..60).joinToString("\n") { "test $it" }
        val offset = -LINE_HEIGHT * text.count { it == '\n' }
        history = uiText(text, WIDTH, HEIGHT-40) {
            textAlignment = TextAlignment.TOP_LEFT
            alignBottomToTopOf(prompt)
            positionY(offset)
        }
//    }

    keys {
        up(Key.TAB) {
            val text = prompt.text
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
            val text = prompt.text
            history.text += processor.process(text) + "\n"
            val offset = -LINE_HEIGHT * history.text.count { it == '\n' }
            history.positionY(offset)
//            history.height = offset.toDouble()
//            history.subList(1, history.size).forEachIndexed { i, line ->
//                history[i] = line
//            }
//            history[history.size - 1] = processor.process(text)
//            (0 until history.size).forEach { i ->
//                (stack[i] as UIText).text = history[i]
//            }
            prompt.text = ""
        }
    }

}