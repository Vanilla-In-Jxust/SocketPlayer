package github.vanilla.socketplayer

import android.app.Activity
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.view.WindowManager
import com.google.common.primitives.Ints
import io.ktor.utils.io.bits.*
import java.net.InetAddress

object Utils {
    fun getIpAddress(wm: WifiManager): InetAddress {
        @Suppress("SpellCheckingInspection")
        // https://stackoverflow.com/questions/1957637/java-convert-int-to-inetaddress
        // @Billchenchina: Ip address is unsigned int.
        // toUInt().toInt() can also do that, but it too lengthy : (
        val bytes = Ints.toByteArray(wm.connectionInfo.ipAddress.reverseByteOrder())
        return InetAddress.getByAddress(bytes)
    }

    fun isCellar(cm: ConnectivityManager): Boolean {
        // https://developer.android.com/training/monitoring-device-state/connectivity-status-type
        return cm.isActiveNetworkMetered
    }

    fun setScreenOn(activity: Activity) {
        // https://developer.android.com/training/scheduling/wakelock#screen
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}
