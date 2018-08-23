package com.example.administrator.hundreddays.activity

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import com.example.administrator.hundreddays.R

import org.jetbrains.anko.find

class StatisticActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistic)

        val layout = find<ConstraintLayout>(R.id.ac_statistic_constrain)
        val dm = resources.displayMetrics
        layout.maxHeight = dm.heightPixels
        layout.minHeight = dm.heightPixels
    }

}
