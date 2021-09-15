package github.vanilla.socketplayer.utils

import android.app.Activity
import android.view.View
import android.view.WindowManager

object UiUtils {
    fun setScreenOn(activity: Activity) {
        // https://developer.android.com/training/scheduling/wakelock#screen
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    // https://developer.android.com/training/system-ui/navigation#40
    fun hideNavBar(activity: Activity) {
        activity.window.decorView.apply {
            systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }
    }
}
