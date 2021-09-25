package github.vanilla.socketplayer.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import github.vanilla.socketplayer.ControlMediaServer
import github.vanilla.socketplayer.SocketService
import github.vanilla.socketplayer.databinding.ActivityMainBinding
import github.vanilla.socketplayer.utils.FileUtils
import github.vanilla.socketplayer.utils.NetworkUtils
import github.vanilla.socketplayer.utils.NetworkUtils.getIpAddress
import github.vanilla.socketplayer.utils.UiUtils
import io.ktor.utils.io.bits.*
import kotlinx.coroutines.*


@DelicateCoroutinesApi
class MainActivity : AppCompatActivity() {
    // https://developer.android.com/topic/libraries/view-binding#activities
    // https://developer.android.com/codelabs/exoplayer-intro#2
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        UiUtils.setScreenOn(this)
        FileUtils.grantPermission(this)

        // https://stackoverflow.com/questions/59419653/cannot-start-activity-background-in-android-10-android-q
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !Settings.canDrawOverlays(this)) {
            requestPermission()
        }

        // https://developer.android.com/training/basics/network-ops/reading-network-state#instantaneous
        val connectivityManager = getSystemService(ConnectivityManager::class.java)
        // https://developer.android.com/training/basics/network-ops/reading-network-state#listening-events
        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                val address = getIpAddress(getSystemService(WIFI_SERVICE) as WifiManager)
                if (NetworkUtils.isCellar(getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager)) return

                // https://www.kotlincn.net/docs/reference/coroutines/basics.html
                Thread { ControlMediaServer.run(this@MainActivity) }.start()

                val outerThis = this@MainActivity
                val serviceIntent = Intent(outerThis, SocketService::class.java)
                ContextCompat.startForegroundService(outerThis, serviceIntent)

                runOnUiThread { binding.networkInfo.text = "telnet ${address.hostAddress} 2323" }
            }

            override fun onLost(network: Network) {
                runOnUiThread { binding.networkInfo.text = "Wifi connection lost. " }
            }
        })
    }

    private fun requestPermission() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:" + this.packageName)
        )
        startActivity(intent)
    }
}