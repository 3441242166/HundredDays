package com.example.administrator.hundreddays.adapter

import android.content.Context
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.administrator.hundreddays.R
import com.example.administrator.hundreddays.bean.PlanIng


class PlanAdapter(data: MutableList<PlanIng>?,val context: Context) : BaseQuickAdapter<PlanIng, BaseViewHolder>(R.layout.item_plan, data) {


    override fun convert(helper: BaseViewHolder, item: PlanIng) {

        helper.setText(R.id.item_plan_title, item.plan?.title)
        helper.setText(R.id.item_plan_keepday, item.insistentDay.toString())
        Glide.with(context)
                .load(item.plan?.imgPath)
                .into(helper.getView(R.id.item_plan_image))

        if(item.isFinish)
            helper.setText(R.id.item_plan_sign, "今日已签到")

        helper.addOnClickListener(R.id.item_plan_sign)
    }
}

