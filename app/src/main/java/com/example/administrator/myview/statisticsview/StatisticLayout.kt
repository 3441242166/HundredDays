package com.example.administrator.myview.statisticsview

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import com.example.administrator.hundreddays.R

class StatisticLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        bindView()
    }

    private fun bindView() {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.view_statistic,this)

    }

}