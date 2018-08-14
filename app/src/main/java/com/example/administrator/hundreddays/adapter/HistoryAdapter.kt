package com.example.administrator.hundreddays.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.animation.Transformation
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.administrator.hundreddays.R
import com.example.administrator.hundreddays.base.BaseApplication
import com.example.administrator.hundreddays.bean.History
import com.example.administrator.hundreddays.bean.PlanIng

class HistoryAdapter(data: MutableList<History>?,val context: Context) : BaseQuickAdapter<History, BaseViewHolder>(R.layout.item_history, data) {

    constructor(context: Context):this(null,context)

    override fun convert(helper: BaseViewHolder, item: History) {

        helper.setText(R.id.item_history_title, item.plan?.title)
        helper.setText(R.id.item_history_date, item.plan?.createDateTime)
        Glide.with(BaseApplication.context)
                .load(item.plan?.imgPath)
                .into(helper.getView(R.id.item_history_img))
        if(item.isFinish == 1){
            Glide.with(BaseApplication.context)
                    .load(R.drawable.icon_finish)
                    .into(helper.getView(R.id.item_history_finish))
        }else{
            Glide.with(BaseApplication.context)
                    .load(R.drawable.icon_unfinish)
                    .into(helper.getView(R.id.item_history_finish))
        }

        helper.setText(R.id.item_history_days, item.keepDay.toString())
    }
}

