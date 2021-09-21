package github.vanilla.socketplayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import github.vanilla.socketplayer.activities.MainActivity
import kotlinx.coroutines.DelicateCoroutinesApi


@DelicateCoroutinesApi
// https://stackoverflow.com/questions/6391902/how-do-i-start-my-app-on-startup
class BootCompleteReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val i = Intent(context, MainActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(i)
        }
    }
}
