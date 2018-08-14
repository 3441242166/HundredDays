package com.example.administrator.hundreddays.view

import com.example.administrator.hundreddays.bean.Plan

interface PlanMessageView {

    fun returnMessage(plan:Plan,totalSign:Int,state:String,isFinish:Boolean)

}