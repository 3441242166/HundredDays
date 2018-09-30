package com.example.administrator.hundreddays.view

import android.graphics.Bitmap
import com.example.administrator.hundreddays.base.BaseView
import com.example.administrator.hundreddays.bean.History

interface MainView : BaseView<MutableList<History>>{

    fun setBackground(path: String)

    fun setMessageDialog(str:String)

    fun setData(data:MutableList<History>)

    fun setMessage(title:String,index:String)

    fun setVisibility(boolean: Boolean)

    fun signSuccess(pos:Int)
}