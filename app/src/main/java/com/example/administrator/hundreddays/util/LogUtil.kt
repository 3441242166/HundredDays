package com.example.administrator.hundreddays.util

import android.util.Log
import android.view.View

fun log(clazz: Class<Any>,message:String){
    Log.i(clazz.name,message)
}

fun onMeasureName(num:Int):String{

    when(num){
        View.MeasureSpec.UNSPECIFIED->{
            return "UNSPECIFIED"
        }
        View.MeasureSpec.EXACTLY->{
            return "EXACTLY"
        }
        View.MeasureSpec.AT_MOST->{
            return "AT_MOST"
        }
    }

    return ""
}