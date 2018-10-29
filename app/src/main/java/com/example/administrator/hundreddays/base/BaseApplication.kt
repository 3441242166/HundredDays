package com.example.administrator.hundreddays.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import io.realm.Realm
import io.realm.RealmConfiguration
import android.util.DisplayMetrics
import android.util.Log
import com.example.administrator.hundreddays.constant.SCREEN_HEIGHT
import com.example.administrator.hundreddays.constant.SCREEN_WIDTH
import com.example.administrator.hundreddays.util.saveToSharedPreferences


class BaseApplication : Application() {
    private val TAG = "BaseApplication"

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        Realm.init(this)
        val config = RealmConfiguration.Builder().build()
        Realm.setDefaultConfiguration(config)
        val dm = resources.displayMetrics
        val heigth = dm.heightPixels
        val width = dm.widthPixels
        saveToSharedPreferences(SCREEN_HEIGHT, heigth.toString())
        saveToSharedPreferences(SCREEN_WIDTH, width.toString())
    }

    companion object {
        /**
         * 全局的上下文
         */

        @SuppressLint("StaticFieldLeak")
                /**
         * 获取context
         * @return
         */
        lateinit var context: Context
            private set
    }

}