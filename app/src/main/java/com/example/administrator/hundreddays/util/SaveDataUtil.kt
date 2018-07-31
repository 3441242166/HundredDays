package com.example.administrator.hundreddays.util

import android.content.Context
import com.example.administrator.hundreddays.base.BaseApplication
import com.example.administrator.hundreddays.constant.FILE_NAME

/**
 * 保存一对键值对到指定的SharedPreferences文件中
 * @param context
 * @param key
 * @param value
 * @return
 */
fun saveToSharedPreferences(key: String, value: String,context: Context = BaseApplication.context): Boolean {
    return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).edit().putString(key ,value).commit()
}

/**
 * 从指定的文件中读取指定key的value
 * @param context
 * @param key
 * @return
 */
fun getValueFromSharedPreferences(key: String,context: Context = BaseApplication.context): String? {
    return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).getString(key,null)
}