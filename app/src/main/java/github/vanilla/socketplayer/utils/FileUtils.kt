package github.vanilla.socketplayer.utils

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.os.Environment
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.core.content.PermissionChecker.checkSelfPermission
import java.io.File
import java.io.FileReader

object FileUtils {
    private val sdcard: File by lazy { Environment.getExternalStorageDirectory() }
    private const val BASE_PATH = "/Pictures/WeChat/"

    // https://stackoverflow.com/questions/2902689/how-can-i-read-a-text-file-from-the-sd-card-in-android
    fun getFileOrThrow(fileName: String): File {
        val possibleFile = File(sdcard.absolutePath + BASE_PATH + fileName)

        // Wrap it as FileReader can detect file existence.
        return kotlin.runCatching { FileReader(possibleFile); possibleFile }
            .getOrElse { throw AssertionError("File: $fileName not found! ") }
    }

    fun grantPermission(activity: Activity) {
        if (checkSelfPermission(activity, READ_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(READ_EXTERNAL_STORAGE), 0);
        }
    }
}