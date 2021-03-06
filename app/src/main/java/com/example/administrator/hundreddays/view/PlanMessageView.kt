package com.example.administrator.hundreddays.view

import android.graphics.Bitmap
import com.example.administrator.hundreddays.bean.Plan
import com.example.administrator.hundreddays.bean.Sign

interface PlanMessageView {

    fun returnMessage(plan: Plan, totalSign: Int, state: String, isFinish: Boolean)
    fun setSignData(signMap: MutableMap<String, Sign>)
    fun setBlurBck(bitmap: Bitmap)
    fun setBck(bitmap: Bitmap)
}