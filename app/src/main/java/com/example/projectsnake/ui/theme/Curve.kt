package com.example.projectsnake.ui.theme

class Curve(_limit:Float) {
    var points: List<Vector> = emptyList()
    var limit: Float = 0f
    init{
        points = emptyList()
        limit = _limit
    }

    fun add(value:Vector){
        points = listOf(value) + points
        while(length() > limit)
            points = points.minus(points.last())
    }
    fun end(value: Float): Vector{
        var index = -1
        var length = 0f
        while (length < value){
            index += 1
            if(index + 1 >= points.size)
                return points.last()
            length += (points[index] - points[index+1]).length()
        }
        return points[index+1] + (points[index] - points[index+1]).normal() *
                (length - value)
    }
    fun length():Float{
        var length = 0f
        for(index in 0..points.size - 2){
            length += (points[index] - points[index+1]).length()
        }
        return length
    }
}