package com.example.administrator.hundreddays.bean

import android.os.Parcel
import android.os.Parcelable

data class Plan(var ID: Long? = null, var title: String, var remindTime: String, var imgPath: String, var createDateTime: String, var frequentDay: Int, var targetDay: Int) : Parcelable {
    constructor() : this(null, "", "", "", "", 1, 30)

    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readInt(),
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(ID)
        writeString(title)
        writeString(remindTime)
        writeString(imgPath)
        writeString(createDateTime)
        writeInt(frequentDay)
        writeInt(targetDay)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Plan> = object : Parcelable.Creator<Plan> {
            override fun createFromParcel(source: Parcel): Plan = Plan(source)
            override fun newArray(size: Int): Array<Plan?> = arrayOfNulls(size)
        }
    }
}