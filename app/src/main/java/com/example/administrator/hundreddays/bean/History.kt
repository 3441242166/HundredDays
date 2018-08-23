package com.example.administrator.hundreddays.bean

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class History(@PrimaryKey var id:String?,
                   var keepDay:Int,
                   var lastSignDate:String,
                   var state:String,
                   var plan: Plan?): RealmObject() {
    constructor() : this(null, 0,"", "",null)

}