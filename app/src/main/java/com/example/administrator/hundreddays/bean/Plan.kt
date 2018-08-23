package com.example.administrator.hundreddays.bean

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Plan(@PrimaryKey var ID: String? = null
                , var title: String,
                var remindTime: String
                ,var imgPath: String,
                var createDateTime: String,
                var frequentDay: Int,
                var targetDay: Int) : RealmObject() {

    constructor() : this(null, "", "", "", "", 1, 30)

}