package com.example.administrator.hundreddays.bean

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class Sign(@PrimaryKey var id:String?,
                var message:String,
                var date :String,
                var planId:String,
                var plan: Plan?): RealmObject() {
    constructor() : this(null, "", "","", null)
}
