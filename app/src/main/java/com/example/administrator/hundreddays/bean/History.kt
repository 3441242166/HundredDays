package com.example.administrator.hundreddays.bean

data class History(var id:Long?, var keepDay:Int, var isFinish:Int, var plan: Plan?) {
    constructor(id: Long?) : this(id, 0,0, null)
}

