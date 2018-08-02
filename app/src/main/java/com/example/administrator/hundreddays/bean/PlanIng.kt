package com.example.administrator.hundreddays.bean

import android.os.Parcel
import android.os.Parcelable


data class PlanIng(var id: Long?, var insistentDay: Int, var lastSignDay: String, var plan: Plan?, var isFinish: Boolean = false) : Parcelable {
    constructor(id: Long?) : this(id, 0, "", null)

    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readInt(),
            source.readString(),
            source.readParcelable<Plan>(Plan::class.java.classLoader),
            1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(id)
        writeInt(insistentDay)
        writeString(lastSignDay)
        writeParcelable(plan, 0)
        writeInt((if (isFinish) 1 else 0))
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<PlanIng> = object : Parcelable.Creator<PlanIng> {
            override fun createFromParcel(source: Parcel): PlanIng = PlanIng(source)
            override fun newArray(size: Int): Array<PlanIng?> = arrayOfNulls(size)
        }
    }
}

