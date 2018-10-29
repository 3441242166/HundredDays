package com.example.administrator.hundreddays.adapter

import android.content.Context
import android.widget.Button
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.administrator.hundreddays.R
import com.example.administrator.hundreddays.bean.Plan
import com.example.administrator.hundreddays.util.DATETYPE
import com.example.administrator.hundreddays.util.getNowString


class PlanAdapter(data: MutableList<Plan>?, val context: Context) : BaseQuickAdapter<Plan, BaseViewHolder>(R.layout.item_plan, data) {


    override fun convert(helper: BaseViewHolder, item: Plan) {

        helper.setText(R.id.item_plan_title, item.title)
        helper.setText(R.id.item_plan_keepday, item.signDays.toString())
        Glide.with(context)
                .load(item.imgPath)
                .into(helper.getView(R.id.item_plan_image))

        val button = helper.getView<Button>(R.id.item_plan_sign)

        if(item.lastSignDate == getNowString(DATETYPE.DATE_DATE)) {
            button.setBackgroundResource(R.drawable.shap_signed)
            button.text = "今日已签到"
        }

        helper.addOnClickListener(R.id.item_plan_sign)
    }
}

