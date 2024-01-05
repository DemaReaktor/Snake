package com.example.projectsnake

import android.annotation.SuppressLint
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.Choreographer
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.example.projectsnake.ui.theme.Friend
import com.example.projectsnake.ui.theme.SqlLite
import com.example.projectsnake.ui.theme.Vector
import kotlin.system.exitProcess

class MainActivity : ComponentActivity() {
    private var snake: Snake? = null
    private var sound: Boolean = true
    private var self: MainActivity = this
    var db: SqlLite? = null

    @RequiresApi(api = Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            setContentView(R.layout.main)
        }
        db = SqlLite(self)
        db?.add(Friend("Oh",true))
    }

    fun exit(view: View) {
        finish()
        exitProcess(0)
    }

    fun settings(view: View) {
        setContentView(R.layout.settings)
    }

    fun back_menu(view: View) {
        if(view.id == R.id.play)
            delete()
        setContentView(R.layout.main)
    }

    fun audio(view: View){
        sound = !sound
        if(sound){
            (view as ImageButton).setImageResource(android.R.drawable.ic_lock_silent_mode)
            val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0)
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0)
        }
                else{
            (view as ImageButton).setImageResource(android.R.drawable.ic_lock_silent_mode_off)
            val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0)
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 100, 0)
        }
    }

    fun on_move_event(e: MotionEvent) : Boolean{
        snake?.let {
            it.move(e)
        }
        return true
    }
    var apples: List<ImageView> = emptyList()
    var last_frame: Long = 0

    fun    frame_callback () : Choreographer.FrameCallback {
        return object : Choreographer.FrameCallback{
         override fun doFrame(it: Long) {
            if(snake == null)
                return
            val layout: RelativeLayout = findViewById(R.id.field)
            val time :Long = it / 1_000_000
            if(time >= 5000L + last_frame){
                last_frame = time
                val position = Vector((0..layout.width).random().toFloat(),
                    (0..layout.height).random().toFloat())
                apples = apples.plus(ImageView(self))
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
            Choreographer.getInstance().postFrameCallback(frame_callback())
        }
    }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun play(view: View){
        setContentView(R.layout.play)
        val layout: RelativeLayout = findViewById(R.id.field)

        Choreographer.getInstance().postFrameCallback(frame_callback())
        layout.setOnTouchListener { _, event ->
            on_move_event(event)
        }
        snake?.die()
        snake = Snake(resources.getIdentifier("own","color",packageName),
            this, layout, Vector(layout.width/2f,layout.height/3f*2))
    }
    fun delete(){
        val layout: RelativeLayout = findViewById(R.id.field)
        for (apple in apples){
            apples = apples.minus(apple)
            layout.removeView(apple)
        }
        snake?.die()
        Choreographer.getInstance().removeFrameCallback(frame_callback())
        layout.setOnTouchListener(null)
        snake = null
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    fun friends(view:View?){
        setContentView(R.layout.friends)
        var friends = db?.let{
            it.get_all()
        }
        val column_list = findViewById<LinearLayout>(R.id.column_list)

        if(friends != null)
        for(friend in friends){
            var column = LayoutInflater.from(this).inflate(
                R.layout.column, null, false)
            column.findViewById<TextView>(R.id.name).text = friend.name
            if(friend.status)
                column.findViewById<TextView>(R.id.status).text = "online"
            else
                column.findViewById<TextView>(R.id.status).text = "offline"
            column_list.addView(column)
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.R)
    fun add_friend(view: View){
        var text = findViewById<EditText>(R.id.add_name)
        text.isEnabled = !text.isEnabled

        if(!text.isEnabled)
        {
            var data = text.text.toString()
            var result: Boolean = false
            var friends = db?.let{
                it.get_all()
            }
            friends?.let {
                result = data.length > 2 && !friends.any({it.name == data})
            }
            if(result)
                db?.let {
                    it.add(Friend(text.text.toString(),false))
                }
            text.alpha = 0f
            text.setText(null)
            friends(null)
        }
        else
            text.alpha = 1f
    }
}