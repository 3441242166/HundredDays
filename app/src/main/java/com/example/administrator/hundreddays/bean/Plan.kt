package com.example.administrator.hundreddays.bean

import com.example.administrator.hundreddays.constant.PLAN_ING
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Plan(@PrimaryKey var id: String? = null,
                var title: String,
                var remindTime: String,
                var imgPath: String,
                var createDateTime: String,
                var frequentDay: Int,
                var targetTimes: Int,
                var planState: String,
                var signDays: Int,
                var lastSignDate: String) : RealmObject() {

    constructor() : this(null, "", "", "", "", 1, 30,PLAN_ING,0,"")

}