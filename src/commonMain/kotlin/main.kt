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
    lateinit var history: UIText
    uiScrollable(width = WIDTH, height = HEIGHT - 40.0) {
        history = uiText("", WIDTH, HEIGHT - 40.0)
    }

    val prompt = uiTextInput(">", width = WIDTH) {
        onTextUpdated {
            println(it.text)
        }
        alignBottomToBottomOf(parent!!)
        focus()
    }


    keys {
        up(Key.ENTER) {
            history.text += prompt.text.substring(1) + "\n"
            prompt.text = ">"
        }
    }

}