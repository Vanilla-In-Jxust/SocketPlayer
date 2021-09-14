package github.vanilla.socketplayer

import android.util.Log
import github.vanilla.socketplayer.databinding.ActivityMainBinding
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.net.InetSocketAddress

// https://ktor.io/docs/servers-raw-sockets.html#client
object SimpleEchoServer {
    private var serverInit: Boolean = false

    @DelicateCoroutinesApi
    fun run(activity: MainActivity, binding: ActivityMainBinding) = runBlocking {
        if (serverInit) return@runBlocking

        val server = aSocket(ActorSelectorManager(Dispatchers.IO)).tcp()
            .bind(InetSocketAddress("0.0.0.0", 2323))
        Log.d("TAG", "Started echo telnet server at ${server.localAddress}")
        serverInit = true

        while (true) {
            val socket = server.accept()

            launch {
                Log.d("TAG", "Socket accepted: ${socket.remoteAddress}")

                val input = socket.openReadChannel()
                val output = socket.openWriteChannel(autoFlush = true)

                try {
                    while (true) {
                        val line = input.readUTF8Line()
                        output.writeStringUtf8("$line\r\n")
                        // activity.runOnUiThread { binding.networkInfo.text = line }
                    }
                } catch (ignored: Throwable) {
                }
            }
        }
    }
}
