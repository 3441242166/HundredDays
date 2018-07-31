package com.example.administrator.hundreddays.bean


data class Sign(var id:Long?,var message:String,var date :String,var plan: Plan?) {
    constructor(id: Long?) : this(id, "", "", null)
}
