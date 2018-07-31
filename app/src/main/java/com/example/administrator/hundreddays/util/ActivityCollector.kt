package com.example.administrator.hundreddays.util

import android.app.Activity
import java.util.ArrayList

/**
 * Activity集合类统一管理Activity
 */

object ActivityCollector {
    private val mActivityList = ArrayList<Activity>()

    /**
     * 添加一个Activity
     * @param activity
     */
    fun addActivity(activity: Activity) {
        mActivityList.add(activity)
    }

    /**
     * 移除一个Activity
     * @param activity
     */
    fun removeActivity(activity: Activity) {
        mActivityList.remove(activity)
    }

    /**
     * finish所有的Activity
     */
    fun finishAll() {
        for (activity in mActivityList) {
            if (!activity.isFinishing) {
                activity.finish()
            }
        }
    }
}