package github.vanilla.socketplayer.utils

import android.app.Activity
import android.net.Uri

object ResUtils {
    @Throws(AssertionError::class)
    fun getUriInRaw(fileName: String, activity: Activity): Uri {
        // https://stackoverflow.com/questions/15912825/how-to-read-file-from-res-raw-by-name
        val rawResId = activity.resources.getIdentifier(fileName, "raw", activity.packageName)
        if (rawResId == 0) throw AssertionError("Resource $fileName does not exist! ")

        // https://stackoverflow.com/questions/16791439/android-how-to-get-uri-from-raw-file=
        return Uri.parse("android.resource://${activity.packageName}/${rawResId}")
    }
}
