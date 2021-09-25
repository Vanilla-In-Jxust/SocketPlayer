package github.vanilla.socketplayer

import android.app.Service
import android.content.Intent
import github.vanilla.socketplayer.activities.PlayerActivity
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import java.net.InetSocketAddress


// https://ktor.io/docs/servers-raw-sockets.html#client
object ControlMediaServer {
    private var serverInit: Boolean = false

    @DelicateCoroutinesApi
    suspend fun run(service: Service) {
        if (serverInit) return

        val server = aSocket(ActorSelectorManager(Dispatchers.IO)).tcp()
            .bind(InetSocketAddress("0.0.0.0", 2323))
        serverInit = true

        while (true) {
            val socket = server.accept()
            val input = socket.openReadChannel()

            try {
                while (true) {
                    val line = input.readUTF8Line()
                    if (line == null) { // line == null means some socket disconnected.
                        delay(1000L)
                        continue
                    }

                    // https://stackoverflow.com/questions/3606596/android-start-activity-from-service
                    val dialogIntent = Intent(service, PlayerActivity::class.java)
                    dialogIntent.putExtra("fileName", line)
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    service.startActivity(dialogIntent)
                }
            } catch (ignored: Throwable) {
            }
        }
    }
}
