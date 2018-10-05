package com.example.administrator.hundreddays.view

import android.graphics.Bitmap
import com.example.administrator.hundreddays.base.BaseView
import com.example.administrator.hundreddays.bean.History

interface MainView : BaseView<MutableList<History>>{
    /**
     * 为指定 pos iamgeview 设置背景图片
     */
    fun setBackground(pos:Int,path: String)

    fun setMessageDialog(str:String)
    /**
     * 为 adapter 设置数据
     */
    fun setData(data:MutableList<History>)
    /**
     * 设置
     */
    fun setMessage(pos:Int,title:String)
    /**
     * 设置界面 view 的显示
     */
    fun setVisibility(boolean: Boolean)
    /**
     * 指定 item 签到成功
     */
    fun signSuccess(pos:Int)
    /**
     * 设置指定 index View的透明度
     */
    fun setViewAlpha(index:Int,alpha:Float)

    fun setIndex(index: String)
}