package github.vanilla.socketplayer

import android.app.Activity
import android.net.Uri

object ResUtils {
    // https://stackoverflow.com/questions/15912825/how-to-read-file-from-res-raw-by-name
    fun getRawResId(fileName: String, activity: Activity): Int {
        return activity.resources.getIdentifier(fileName, "raw", activity.packageName)
    }

    fun checkResIdValid(resId: Int, activity: Activity): Boolean {
        if (resId == 0) return false
        return kotlin.runCatching { activity.resources.openRawResource(resId) }.isSuccess
    }

    fun getUriInRaw(resId: Int, activity: Activity): Uri {
        if (!checkResIdValid(resId, activity)) throw AssertionError()
        // https://stackoverflow.com/questions/16791439/android-how-to-get-uri-from-raw-file
        return Uri.parse("android.resource://${activity.packageName}/${resId}")
    }
}
