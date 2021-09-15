package github.vanilla.socketplayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import github.vanilla.socketplayer.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {
    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityPlayerBinding.inflate(layoutInflater)
    }

    private val resId by lazy { intent.getIntExtra("resId", 0) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        Utils.setScreenOn(this)

        // https://developer.android.com/training/basics/firstapp/starting-activity#DisplayMessage
        if (!ResUtils.checkResIdValid(resId, this)) finish()
    }

    private var player: SimpleExoPlayer? = null
    override fun onStart() {
        super.onStart()

        // initializePlayer()
        player = SimpleExoPlayer.Builder(this).build()
            .also { exoPlayer -> viewBinding.videoView.player = exoPlayer }
            .also { exoPlayer ->
                val resUri = ResUtils.getUriInRaw(resId, this)
                exoPlayer.setMediaItem(MediaItem.fromUri(resUri))
            }.also { exoPlayer ->
                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.seekTo(currentWindow, playbackPosition)
                exoPlayer.prepare()
            }
    }

    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L
    override fun onStop() {
        super.onStop()

        // releasePlayer()
        player?.run {
            playbackPosition = this.currentPosition
            currentWindow = this.currentWindowIndex
            playWhenReady = this.playWhenReady
            release()
        }

        player = null
    }
}
