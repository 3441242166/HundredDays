package com.example.administrator.hundreddays.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
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