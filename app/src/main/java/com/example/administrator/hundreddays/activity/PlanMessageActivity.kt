package com.example.administrator.hundreddays.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.widget.NestedScrollView
import android.util.Log
import android.view.View
import com.example.administrator.hundreddays.R
import com.example.administrator.hundreddays.base.BaseActivity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.administrator.hundreddays.bean.Plan
import com.example.administrator.hundreddays.bean.Sign
import com.example.administrator.hundreddays.constant.DATA
import com.example.administrator.hundreddays.presenter.PlanMessagePresenter
import com.example.administrator.hundreddays.view.PlanMessageView
import com.example.administrator.myview.MyCalendar
import kotlinx.android.synthetic.main.activity_plan_message.*
import org.jetbrains.anko.find

class PlanMessageActivity : BaseActivity() ,PlanMessageView{
    private val TAG = "PlanMessageActivity"

    val presenter = PlanMessagePresenter(this,this)

    private lateinit var title:TextView
    private lateinit var message:TextView
    private lateinit var ing:TextView
    private lateinit var sign:TextView
    private lateinit var bck:ImageView
    private lateinit var blurBck:ImageView
    private lateinit var headLayout:ConstraintLayout
    private lateinit var layout:LinearLayout
    private lateinit var nestedLayout: NestedScrollView
    private lateinit var calendar: MyCalendar

    override val contentView: Int get() = R.layout.activity_plan_message

    override fun init(savedInstanceState: Bundle?) {
        initView()
        initEvent()
        presenter.initData(intent.getStringExtra(DATA))
        presenter.lateInit()
    }

    private fun initEvent() {
        nestedLayout.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            //Log.i(TAG,"nX=$scrollX nY=$scrollY ox=$oldScrollX oy=$oldScrollY")
            if(scrollY<300){
                val max = 300
                bck.imageAlpha = 255 - (255*scrollY/max)
            }else{
                bck.imageAlpha = 0
            }
        }
    }

    private fun initView() {
        title = find(R.id.ac_message_title)
        message = find(R.id.ac_message_msg)
        ing = find(R.id.ac_message_ing)
        sign = find(R.id.ac_message_sign)
        bck = find(R.id.ac_message_bck)
        blurBck = find(R.id.ac_message_blurbck)
        headLayout = find(R.id.ac_message_head)
        layout = find(R.id.ac_message_layout)
        nestedLayout = find(R.id.ac_message_nested)
        calendar = find(R.id.ac_message_calendar)

        val dm = resources.displayMetrics
        headLayout.maxHeight = dm.heightPixels
        headLayout.minHeight = dm.heightPixels
    }

    override fun returnMessage(plan: Plan, totalSign: Int, state: String, isFinish: Boolean) {
        title.text = plan.title
        Glide.with(this).load(plan.imgPath).transition(DrawableTransitionOptions().crossFade()).apply(RequestOptions()).into(bck)

        sign.text = "$totalSign 签到"
        message.text = if(isFinish) "今日完成" else "未签到"
        ing.text = state

    }

    override fun setSignData(signMap: MutableMap<String, Sign>) {
        calendar.setData(signMap)
    }

    override fun setBlurBck(blurImageView: Bitmap) {
        blurBck.setImageBitmap(blurImageView)
    }
}

