package github.vanilla.socketplayer

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.wifi.WifiManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import github.vanilla.socketplayer.databinding.ActivityMainBinding
import io.ktor.utils.io.bits.*
import kotlinx.coroutines.*

@DelicateCoroutinesApi
class MainActivity : AppCompatActivity() {
    // https://developer.android.com/topic/libraries/view-binding#activities
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Utils.setScreenOn(this)

        // https://developer.android.com/training/basics/network-ops/reading-network-state#instantaneous
        val connectivityManager = getSystemService(ConnectivityManager::class.java)
        // https://developer.android.com/training/basics/network-ops/reading-network-state#listening-events
        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                val wm = getSystemService(Context.WIFI_SERVICE) as WifiManager
                val address = Utils.getIpAddress(wm)

                val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                if (Utils.isCellar(cm)) return

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