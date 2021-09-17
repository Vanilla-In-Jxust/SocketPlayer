package github.vanilla.socketplayer

import android.content.Intent
import github.vanilla.socketplayer.activities.MainActivity
import github.vanilla.socketplayer.activities.PlayerActivity
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
    fun run(activity: MainActivity) = runBlocking {
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

                        // https://developer.android.com/training/basics/firstapp/starting-activity#BuildIntent
                        Intent(activity, PlayerActivity::class.java).apply {
                            putExtra("fileName", line)
                            activity.runOnUiThread { activity.startActivity(this) }
                        }
                    }
                } catch (ignored: Throwable) {
                }
            }
        }
    }
}
