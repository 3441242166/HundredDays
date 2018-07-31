package com.example.administrator.hundreddays.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.administrator.hundreddays.util.ActivityCollector

abstract class BaseActivity : AppCompatActivity() {

    protected abstract val contentView: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentView)
        ActivityCollector.addActivity(this)
        init(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }

    abstract fun init(savedInstanceState: Bundle?)

}