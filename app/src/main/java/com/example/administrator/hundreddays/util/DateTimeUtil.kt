package com.example.administrator.hundreddays.util

import android.annotation.SuppressLint
import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

private val TAG = "DateUtil"

enum class DATETYPE(val str:String){
    DATE_TIME("yyyy-MM-dd HH:mm:ss"),
    DATE_DATE("yyyy-MM-dd"),
    DATE_MONTH("yyyy-MM"),
    TIME("HH:mm:ss"),
}

fun getNowString(type:DATETYPE = DATETYPE.DATE_TIME):String{
    val currentTime = Date()
    val formatter = SimpleDateFormat(type.str)
    val dateString = formatter.format(currentTime)
    Log.i(TAG, "getNowDateTimeString    $dateString")
    return dateString
}

fun getDateString(date:Date,type: DATETYPE = DATETYPE.DATE_TIME): String {
    val formatter = SimpleDateFormat(type.str)
    val dateString = formatter.format(date)
    Log.i(TAG, dateString)
    return dateString
}

fun getDateString(dateTime: String,type: DATETYPE = DATETYPE.DATE_TIME): String {
    val formatter = SimpleDateFormat(type.str)
    var date = Date()
    try {
        date = formatter.parse(dateTime)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    val dateString = formatter.format(date)
    Log.i(TAG, "getNowTimeString    $dateString")
    return dateString
}

fun getDateTimeString(date: String, time: String): String {
    var dateTime = Date()
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    try {
        dateTime = formatter.parse("$date $time")
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    val dateString = formatter.format(dateTime)
    Log.i("date", "getNowDateTimeString    $dateString")
    return dateString
}

fun getDateByDateTimeString(date: String): Date {
    var mDate = Date()
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
    try {
        mDate = format.parse(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return mDate
}

fun getDateByDateString(date: String): Date {
    var mDate = Date()
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
    try {
        mDate = format.parse(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return mDate
}

fun differentDay(bdate: String, smdate: String): Int {

    val sdf = SimpleDateFormat("yyyy-MM-dd")

    try {
        val d1 = sdf.parse(bdate)
        val d2 = sdf.parse(smdate)

        val diff = d1.time - d2.time
        val days = diff / (1000 * 60 * 60 * 24)
        return days.toInt()
    } catch (e: Exception) {
        return -1
    }

}

fun getAddDayString(date: String, day: Int): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = getDateByDateString(date).time
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    calendar.add(Calendar.DATE, day)

    return sdf.format(calendar.time)

}

fun getTimeLongByString(time: String): Long {
    Log.i(TAG, "getTimeLongByString: time = $time")
    var x = 0
    var sum: Long = 0
    val st = StringTokenizer(time, ":")
    while (st.hasMoreElements()) {
        val temp = st.nextToken()
        sum += when (x) {
            0 -> (Integer.valueOf(temp) * 1000 * 3600).toLong()
            1 -> (Integer.valueOf(temp) * 1000 * 60).toLong()
            else -> (Integer.valueOf(temp) * 1000).toLong()
        }
        x++
    }
    return sum
}

