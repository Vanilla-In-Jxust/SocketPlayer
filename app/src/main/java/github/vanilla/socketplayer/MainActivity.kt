package github.vanilla.socketplayer

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.common.primitives.Ints
import github.vanilla.socketplayer.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import java.net.InetAddress

@DelicateCoroutinesApi
class MainActivity : AppCompatActivity() {
    // https://developer.android.com/topic/libraries/view-binding#activities
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // https://developer.android.com/training/scheduling/wakelock#screen
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // https://developer.android.com/training/basics/network-ops/reading-network-state#instantaneous
        val connectivityManager = getSystemService(ConnectivityManager::class.java)
        // https://developer.android.com/training/basics/network-ops/reading-network-state#listening-events
        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                val wm = getSystemService(Context.WIFI_SERVICE) as WifiManager

                // https://stackoverflow.com/questions/1957637/java-convert-int-to-inetaddress
                val bytes = Ints.toByteArray(wm.connectionInfo.ipAddress)
                val address = InetAddress.getByAddress(bytes)

                // https://developer.android.com/training/monitoring-device-state/connectivity-status-type
                val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                if (cm.isActiveNetworkMetered) return

                // https://www.kotlincn.net/docs/reference/coroutines/basics.html
                Thread { ControlMediaServer.run(this@MainActivity, binding) }.start()
                runOnUiThread { binding.networkInfo.text = "telnet ${address.hostAddress} 2323" }
            }

            override fun onLost(network: Network) {
                runOnUiThread { binding.networkInfo.text = "Wifi connection lost. " }
            }
        })
    }
}