package com.example.administrator.hundreddays.util

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

private val TAG = "DateUtil"

fun getNowDateTimeString(): String {
    val currentTime = Date()
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val dateString = formatter.format(currentTime)
    Log.i(TAG, "getNowDateTimeString    $dateString")
    return dateString
}

fun getNowDateString(): String {
    val currentTime = Date()
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    val dateString = formatter.format(currentTime)
    Log.i(TAG, dateString)
    return dateString
}

//    public static String getStringByDateTime(Date date){
//        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
//        Log.i(TAG, "getStringByDateTime: dateString " +formatter.format(date));
//        return formatter.format(date);
//    }

fun getNowTimeString(): String {
    val currentTime = Date()
    val formatter = SimpleDateFormat("HH:mm:ss")
    val dateString = formatter.format(currentTime)
    Log.i(TAG, dateString)
    return dateString
}

fun getTimeString(dateTime: String): String {
    val formatter = SimpleDateFormat("HH:mm:ss")
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

fun getDateString(dateTime: String): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    var date = Date()
    try {
        date = formatter.parse(dateTime)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    val dateString = formatter.format(date)
    Log.i(TAG, "getNowDateString    $dateString")
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

fun getTimeByDateString(date: String): Date {
    var mDate = Date()
    val format = SimpleDateFormat("HH:mm:ss", Locale.CHINA)
    try {
        mDate = format.parse(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return mDate
}

/*    date_1 大 返回 1 --- date_1 小 返回 -1 ---else 返回 0 */
fun compareStringByDate(date_1: String, date_2: String): Int {
    val df = SimpleDateFormat("yyyy-MM-dd")
    try {
        val dt1 = df.parse(date_1)
        val dt2 = df.parse(date_2)
        return if (dt1.time > dt2.time) {
            1
        } else if (dt1.time < dt2.time) {
            -1
        } else {
            0
        }
    } catch (exception: Exception) {
        exception.printStackTrace()
    }

    return 0
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
        if (x == 0) {
            sum += (Integer.valueOf(temp) * 1000 * 3600).toLong()
        } else if (x == 1) {
            sum += (Integer.valueOf(temp) * 1000 * 60).toLong()
        } else {
            sum += (Integer.valueOf(temp) * 1000).toLong()
        }
        x++
    }
    return sum
}

fun getTimeByLong(num: Long): String {
    val date = Date(num)
    var h = "" + num / 1000 / 60 / 60
    if (h.length < 2) {
        h = "0$h"
    }
    var m = "" + num / 1000 / 60 % 60
    if (m.length < 2) {
        m = "0$m"
    }
    var s = "" + num / 1000 % 60
    if (s.length < 2) {
        s = "0$s"
    }
    return "$h:$m:$s"
}