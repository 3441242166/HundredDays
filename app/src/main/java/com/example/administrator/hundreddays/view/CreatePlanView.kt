package com.example.administrator.hundreddays.view

import android.content.Intent
import android.graphics.Bitmap
import com.example.administrator.hundreddays.base.BaseView
import com.example.administrator.hundreddays.bean.Plan

interface CreatePlanView:BaseView<String> {
    fun startIntent(intent: Intent,code:Int)
    fun before()
    fun success(data: String)
    fun failure(error: Throwable)
    fun after()
    fun setImage(bitmap: Bitmap)
}