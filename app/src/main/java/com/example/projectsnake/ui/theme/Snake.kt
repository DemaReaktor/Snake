package com.example.projectsnake

import kotlin.math.sqrt
import android.media.Image
import android.text.Layout
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.core.view.marginLeft
import kotlin.math.sqrt

class Snake( _color: Int, _main: MainActivity, _layout: RelativeLayout) {
    var last_x: Float
    var last_y: Float
    var color: Int
    var main: MainActivity
    var layout: RelativeLayout

    init{
        color = _color
        main = _main
        last_x = 0f
        last_y = 0f
        layout = _layout
    }

    var body: List<ImageView> = emptyList()
    fun move(value:MotionEvent){
        last_x = value.getX()
        last_y = value.getY()

        if(body.size > 0){
            var normal_x:Float = last_x - body[0].translationX
            var normal_y:Float = last_y - body[0].translationY
            val length: Float = sqrt(normal_x*normal_x + normal_y*normal_y)
            normal_x = normal_x / length * body.size * 10
            normal_y = normal_y / length * body.size * 10

            normal_x += body[0].translationX
            normal_y += body[0].translationY

            for (index in 1..body.size - 1){
                body[index].translationX = body[index - 1].translationX
                body[index].translationY = body[index - 1].translationY
            }

            body[0].translationX = normal_x
            body[0].translationY = normal_y
        }
    }
    fun add(value:Int){
        val image = ImageView(main)
        image.setImageResource(R.drawable.body)
        image.setColorFilter(color)
        image.translationX = last_x
        image.translationY = last_y
        layout.addView(image)
        image.getLayoutParams().width = 50
        image.getLayoutParams().height = 50
        body = body.plus(image)
    }
    fun die(){
        for( element in body){
            layout.removeView(element)
        }
    }
}