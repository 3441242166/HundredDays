package com.example.administrator.hundreddays.adapter

import android.content.Context
import android.widget.Button
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.administrator.hundreddays.R
import com.example.administrator.hundreddays.bean.History
import com.example.administrator.hundreddays.util.DATETYPE
import com.example.administrator.hundreddays.util.getNowString


class PlanAdapter(data: MutableList<History>?,val context: Context) : BaseQuickAdapter<History, BaseViewHolder>(R.layout.item_plan, data) {


    override fun convert(helper: BaseViewHolder, item: History) {

        helper.setText(R.id.item_plan_title, item.plan?.title)
        helper.setText(R.id.item_plan_keepday, item.keepDay.toString())
        Glide.with(context)
                .load(item.plan?.imgPath)
                .into(helper.getView(R.id.item_plan_image))

        val button = helper.getView<Button>(R.id.item_plan_sign)

        if(item.lastSignDate == getNowString(DATETYPE.DATE_DATE)) {
            button.setBackgroundResource(R.drawable.shap_signed)
            button.text = "今日已签到"
        }

        helper.addOnClickListener(R.id.item_plan_sign)
    }
}

