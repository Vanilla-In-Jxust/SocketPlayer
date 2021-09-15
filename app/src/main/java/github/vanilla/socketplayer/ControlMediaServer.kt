package github.vanilla.socketplayer

import android.content.Intent
import android.widget.Toast
import github.vanilla.socketplayer.activities.MainActivity
import github.vanilla.socketplayer.activities.PlayerActivity
import github.vanilla.socketplayer.databinding.ActivityMainBinding
import github.vanilla.socketplayer.utils.ResUtils
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.net.InetSocketAddress

// https://ktor.io/docs/servers-raw-sockets.html#client
object ControlMediaServer {
    private var serverInit: Boolean = false

    @DelicateCoroutinesApi
    fun run(activity: MainActivity, binding: ActivityMainBinding) = runBlocking {
        if (serverInit) return@runBlocking

        val server = aSocket(ActorSelectorManager(Dispatchers.IO)).tcp()
            .bind(InetSocketAddress("0.0.0.0", 2323))
        serverInit = true

        while (true) {
            val socket = server.accept()
            val input = socket.openReadChannel()

            launch {
                try {
                    while (true) {
                        val line = input.readUTF8Line()
                        val resId = ResUtils.getRawResId(line.orEmpty(), activity)

                        if (!ResUtils.checkResIdValid(resId, activity)) {
                            val toastText = "File: ${line}.mp4 not found. "
                            activity.runOnUiThread {
                                Toast.makeText(activity, toastText, Toast.LENGTH_LONG).show()
                            }
                        } else {
                            // https://developer.android.com/training/basics/firstapp/starting-activity#BuildIntent
                            val intent = Intent(activity, PlayerActivity::class.java)
                            intent.putExtra("resId", resId)
                            activity.runOnUiThread { activity.startActivity(intent) }
                        }
                    }
                } catch (ignored: Throwable) {
                }
            }
        }
    }
}
