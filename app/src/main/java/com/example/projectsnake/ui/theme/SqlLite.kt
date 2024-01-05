package com.example.projectsnake.ui.theme

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.P)
class SqlLite(
    context: Context,
) : SQLiteOpenHelper(context, "snake", null, 1) {
    val table = "friends"
    var index: Int = 0
    override fun onCreate(db: SQLiteDatabase?) {
        val x =  "CREATE TABLE ${table} (" +
                "id INTEGER PRIMARY KEY," +
                "name TEXT," +
                "status BOOL)"
        db?.execSQL(x)

        get_all()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    fun add(friend: Friend){
        index += 1
        var cv=ContentValues().apply {
            put("id",index)
            put("name",friend.name)
            put("status",friend.status)
        }
        writableDatabase.insert(table,null,cv)
    }

    fun get_all() : List<Friend>{
        var list:List<Friend> = emptyList()
        val query="SELECT * FROM " + table
        val db=this.readableDatabase
        val result= db.rawQuery(query,null)
        var max = 0
        if(result.moveToNext()){
            do{
                val x = result.getColumnIndex("name")
                val y = result.getColumnIndex("status")
                val mm = result.getColumnIndex("id")
                val m = result.getString(mm).toInt()
                if(m > max)
                    max = m
                list = list.plus(Friend(
                    result.getString(x),
                    result.getString(y).toBoolean(),
                ))
            }
            while (result.moveToNext())
        }
        result.close()
        db.close()
        if (max > index)
            index = max
        return list
    }
}