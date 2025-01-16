import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass

fun WindowSizeClass.isWidthExpanded(): Boolean {
    return this.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED
}