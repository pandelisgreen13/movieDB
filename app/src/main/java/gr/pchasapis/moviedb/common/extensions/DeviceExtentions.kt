import android.annotation.SuppressLint
import android.app.Activity
import android.view.inputmethod.InputMethodManager
import timber.log.Timber

@SuppressLint("WrongConstant")
fun closeSoftKeyboard(activity: Activity) {
    try {
        val inputManager = activity.getSystemService("input_method") as InputMethodManager
        inputManager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 2)
    } catch (ex: Exception) {
        Timber.d(ex)
    }
}