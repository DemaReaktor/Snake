package com.example.projectsnake.ui.theme

import kotlin.math.sqrt
import android.widget.ImageView
import android.widget.RelativeLayout
import com.example.projectsnake.MainActivity
import com.example.projectsnake.R
import kotlin.math.sqrt

class Vector (_x: Float, _y:Float) {
        var x: Float = 0f
        var y: Float = 0f
        init{
            x = _x
            y = _y
        }

    fun length(): Float = sqrt(x*x + y*y)
    fun normal(): Vector = Vector(x/length(),y/length())
    fun distance(value: Vector): Float= (this - value).length()
    operator fun times(value: Float):Vector = Vector(x*value,y*value)
    operator fun div(value: Float):Vector = Vector(x/value,y/value)
    operator fun plus(value: Vector):Vector = Vector(x+value.x,y+value.y)
    operator fun minus(value: Vector):Vector = Vector(x-value.x,y-value.y)
}