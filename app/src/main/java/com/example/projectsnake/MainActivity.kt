package com.example.projectsnake

import android.annotation.SuppressLint
import android.os.Bundle
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
        if(sound)
            (view as ImageButton).setImageResource(android.R.drawable.ic_lock_silent_mode)
                else
            (view as ImageButton).setImageResource(android.R.drawable.ic_lock_silent_mode_off)
    }

    fun on_move_event(e: MotionEvent) : Boolean{
            snake?.move(e)
        return true
    }

    @SuppressLint("ClickableViewAccessibility")
    fun play(view: View){
        setContentView(R.layout.play)
        val layout: RelativeLayout = findViewById(R.id.field)
        layout.setOnTouchListener { _, event ->
            on_move_event(event)
        }
        snake?.die()
        snake = Snake(resources.getIdentifier("own","color",packageName), this, layout)
        snake?.add(R.color.own)
    }

}