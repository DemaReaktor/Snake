package com.example.projectsnake

import kotlin.random.Random
import android.annotation.SuppressLint
import android.media.AudioManager
import android.os.Bundle
import android.view.Choreographer
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.projectsnake.ui.theme.ProjectSnakeTheme
import com.example.projectsnake.ui.theme.Vector
import kotlin.system.exitProcess

class MainActivity : ComponentActivity() {
    private var snake: Snake? = null
    private var sound: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            setContentView(R.layout.main)
        }
    }

    fun exit(view: View) {
        finish()
        exitProcess(0)
    }

    fun settings(view: View) {
        setContentView(R.layout.settings)
    }

    fun back_menu(view: View) {
        setContentView(R.layout.main)
    }

    fun audio(view: View){
        R.drawable.body
        ImageView(this)
        sound = !sound
        if(sound){
            (view as ImageButton).setImageResource(android.R.drawable.ic_lock_silent_mode)
            (getSystemService(AUDIO_SERVICE) as AudioManager).
            setStreamVolume(AudioManager.STREAM_MUSIC,0,AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE)
        }
                else{
            (view as ImageButton).setImageResource(android.R.drawable.ic_lock_silent_mode_off)
            (getSystemService(AUDIO_SERVICE) as AudioManager).
            setStreamVolume(AudioManager.STREAM_MUSIC,100,AudioManager.FLAG_PLAY_SOUND)
        }
    }

    fun on_move_event(e: MotionEvent) : Boolean{
            snake?.move(e)
        return true
    }
    var apples: List<ImageView> = emptyList()
    var last_frame: Long = 0

    @SuppressLint("ClickableViewAccessibility")
    fun play(view: View){
        setContentView(R.layout.play)
        val layout: RelativeLayout = findViewById(R.id.field)
        val this_this = this

        Choreographer.getInstance().postFrameCallback(
            object : Choreographer.FrameCallback {
                override fun doFrame(it: Long) {
                    val layout: RelativeLayout = findViewById(R.id.field)
                    val time :Long = it / 1_000_000
                    if(time >= 5000L + last_frame){
                        last_frame = time
                        val position = Vector((0..layout.width).random().toFloat(),
                            (0..layout.height).random().toFloat())
                        apples = apples.plus(ImageView(this_this))
                        apples.last().setImageResource(R.mipmap.apple)
                        apples.last().translationX = position.x
                        apples.last().translationY = position.y
                        layout.addView(apples.last())
                        apples.last().getLayoutParams().width = 70
                        apples.last().getLayoutParams().height = 70
                    }

                    for (apple in apples){
                        val pos: Vector = snake?.position()?: Vector(0f,0f)
                        val size = snake?.size()?:50
                        if(Vector(apple.translationX,apple.translationY).
                            distance(pos) < size)
                        {
                            snake?.add(R.color.own)
                            apples = apples.minus(apple)
                            layout.removeView(apple)
                        }
                    }
                    Choreographer.getInstance().postFrameCallback(this)
                }}

        )
        layout.setOnTouchListener { _, event ->
            on_move_event(event)
        }
        snake?.die()
        snake = Snake(resources.getIdentifier("own","color",packageName),
            this, layout, Vector(layout.width/2f,layout.height/3f*2))
    }
}