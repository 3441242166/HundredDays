package com.example.administrator.hundreddays.adapter

import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.administrator.hundreddays.R
import com.example.administrator.hundreddays.base.BaseApplication
import com.example.administrator.hundreddays.bean.Plan
import com.example.administrator.hundreddays.bean.PlanIng
import com.example.administrator.hundreddays.util.getBitmapFromLocal


class PlanAdapter(data: MutableList<PlanIng>?) : BaseQuickAdapter<PlanIng, BaseViewHolder>(R.layout.item_plan, data) {

    override fun convert(helper: BaseViewHolder, item: PlanIng) {

        helper.setText(R.id.item_plan_title, item.plan?.title)
        helper.setText(R.id.item_plan_keepday, item.insistentDay.toString())
        Glide.with(BaseApplication.context)
                .load(item.plan?.imgUrl)
                .into(helper.getView(R.id.item_plan_image))

        helper.addOnClickListener(R.id.item_plan_sign)
    }
}

