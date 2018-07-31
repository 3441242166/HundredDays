package com.example.administrator.hundreddays.bean

data class Plan(var ID: Long? = null,var title: String,var remindTime: String,var imgUrl: String,var createDateTime: String,var frequentDay:Int,var targetDay: Int){


    constructor() :this(null, "", "", "", "", 1, 30)


}