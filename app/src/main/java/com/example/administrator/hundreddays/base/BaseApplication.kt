package com.example.administrator.hundreddays.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import io.realm.Realm
import io.realm.RealmConfiguration

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        Realm.init(this)
        val config = RealmConfiguration.Builder().build()
        Realm.setDefaultConfiguration(config)
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