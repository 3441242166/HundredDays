package com.example.administrator.hundreddays.bean


data class PlanIng(var id:Long?,var insistentDay:Int,var lastSignDay :String,var plan: Plan?) {
    constructor(id: Long?) : this(id, 0, "", null)
}
