package com.example.projectsnake

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
    private var sound: Boolean = true
    private var dark_theme: Boolean = false

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
        sound = !sound
        if(sound)
            (view as ImageButton).setImageResource(android.R.drawable.ic_lock_silent_mode)
                else
            (view as ImageButton).setImageResource(android.R.drawable.ic_lock_silent_mode_off)
    }

    fun dark_theme(view: View){
        dark_theme = !dark_theme
        if(dark_theme)
            R.color.background = R.color.background_night
        else
            R.color.background = R.color.background_day
    }
}