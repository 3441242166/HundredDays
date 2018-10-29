package com.example.administrator.hundreddays.adapter

import android.content.Context
import android.view.View
import android.widget.RadioButton
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.administrator.hundreddays.R
import com.example.administrator.hundreddays.bean.Plan
import com.example.administrator.hundreddays.util.DATETYPE
import com.example.administrator.hundreddays.util.getNowString

class HistoryAdapter(data: MutableList<Plan>?, val context: Context) : BaseQuickAdapter<Plan, BaseViewHolder>(R.layout.item_history, data) {

    constructor(context: Context):this(null,context)

    override fun convert(helper: BaseViewHolder, item: Plan) {

        helper.setText(R.id.item_history_title, item.title)
        helper.setText(R.id.item_history_days, item.signDays.toString())

        val view = helper.getView<View>(R.id.item_history_lv)
        val button = helper.getView<RadioButton>(R.id.item_history_sign)
        button.isChecked = getNowString(DATETYPE.DATE_DATE) == item.lastSignDate
    }
}

