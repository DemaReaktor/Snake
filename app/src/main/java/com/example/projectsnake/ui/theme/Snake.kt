package com.example.projectsnake

import android.app.Notification.Action
import android.graphics.PorterDuff
import kotlin.math.sqrt
import android.media.Image
import android.media.MediaPlayer
import android.text.Layout
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.core.view.marginLeft
import com.example.projectsnake.ui.theme.Curve
import com.example.projectsnake.ui.theme.Vector
import kotlin.math.sqrt

class Snake( _color: Int, _main: MainActivity, _layout: RelativeLayout, _vector: Vector) {
    var last: Vector
    var main: MainActivity
    var layout: RelativeLayout
    var body: List<ImageView> = emptyList()
    var curve: Curve
    private var size: Int
    var media: MediaPlayer

    fun size() = size

    init{
        main = _main
        layout = _layout
        last = Vector(0f,0f)
        body = emptyList()
        curve = Curve(50f)
        size = 50
        media = MediaPlayer.create(main,R.raw.chipi)
        add(_color, _vector)
    }
    fun setTransparent(value:Float){
        for (element in body)
            element.alpha = value
    }
    fun move(value:MotionEvent){
        when (value.action){
            MotionEvent.ACTION_UP -> {
                media.stop()
                setTransparent(0f)
            }
            MotionEvent.ACTION_DOWN -> {
                media.start()
                setTransparent(100f)
            }
            else -> {
                if(body.isNotEmpty()){
                    var normal = Vector(value.getX() - body[0].translationX,
                        value.getY() - body[0].translationY)
                    if(normal.length() > size.toFloat()*1.5f){
                        normal = normal.normal() * 50f / (4 + body.size.toFloat())
                        last = normal
                        body[0].translationX += normal.x
                        body[0].translationY += normal.y
                        curve.add(Vector(body[0].translationX, body[0].translationY))
                        for (index in 1..body.size - 1){
                            val end = curve.end(index*50f)
                            body[index].translationX = end.x
                            body[index].translationY = end.y
                        }
                    }
                }
            }
        }

    }
    fun add(value:Int, pos:Vector? = null){
        if(!media.isPlaying){
            media.start()
            media.isLooping = true
        }
        val image = ImageView(main)
        image.setImageResource(R.drawable.body)
        image.setColorFilter(value)
        curve.limit += 50f
        if(pos == null && body.isNotEmpty()){
            image.translationX = body.last().translationX - last.x
            image.translationY = body.last().translationY - last.y
        }
        else
        if (pos != null)
        {
            image.translationX = pos.x
            image.translationY = pos.y
        }
        layout.addView(image)
        image.getLayoutParams().width = 50
        image.getLayoutParams().height = 50
        body = body.plus(image)
        media.setVolume(body.size.toFloat()*10f,body.size.toFloat()*10f)
        change_size((sqrt(body.size.toFloat())*50).toInt())
    }
    fun change_size(size:Int){
        this.size = size
        for (image in body)
        {
            image.getLayoutParams().width = size
            image.getLayoutParams().height = size
        }
    }
    fun position():Vector = Vector(body.first().translationX,body.first().translationY)
    fun die(){
        for( element in body){
            layout.removeView(element)
        }
        body = emptyList()
        curve = Curve(50f)
        media.stop()
    }
}