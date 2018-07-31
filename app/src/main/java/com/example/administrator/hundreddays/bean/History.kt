package com.example.administrator.hundreddays.bean

data class History(var id:Long?,var keppDay:Int,var plan: Plan?) {
    constructor(id: Long?) : this(id, 0, null)
}

