package com.example.administrator.hundreddays.adapter

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.administrator.hundreddays.R
import com.example.administrator.hundreddays.base.BaseApplication
import com.example.administrator.hundreddays.bean.GridBean

class GridAdapter(data: MutableList<GridBean>?) : BaseQuickAdapter<GridBean, BaseViewHolder>(R.layout.item_grid, data) {

    override fun convert(helper: BaseViewHolder, item: GridBean) {
        helper.setText(R.id.item_grid_text, item.title)
        Glide.with(BaseApplication.context)
                .load(item.img)
                .into(helper.getView(R.id.item_grid_image))

    }
}
