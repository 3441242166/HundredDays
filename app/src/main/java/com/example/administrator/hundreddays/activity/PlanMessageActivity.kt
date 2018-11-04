package com.example.administrator.hundreddays.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.support.v4.widget.NestedScrollView
import android.view.ViewStub
import com.example.administrator.hundreddays.R
import com.example.administrator.hundreddays.base.BaseActivity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.administrator.hundreddays.bean.Plan
import com.example.administrator.hundreddays.bean.Sign
import com.example.administrator.hundreddays.constant.DATA
import com.example.administrator.hundreddays.constant.SCREEN_HEIGHT
import com.example.administrator.hundreddays.presenter.PlanMessagePresenter
import com.example.administrator.hundreddays.util.getValueFromSharedPreferences
import com.example.administrator.hundreddays.view.PlanMessageView
import com.example.administrator.myview.PercentView
import com.example.administrator.myview.calendar.PlanCalendar
import org.jetbrains.anko.find
import org.jetbrains.anko.toast

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
    private lateinit var calendar: PlanCalendar
    private lateinit var precentView: PercentView

    override val contentView: Int get() = R.layout.activity_plan_message

    override fun init(savedInstanceState: Bundle?) {
        initView()
        initEvent()
        presenter.initData(intent.getStringExtra(DATA))
        Handler().postDelayed({presenter.lateInit()},100)
    }

    private fun initEvent() {
        val maxPos = getValueFromSharedPreferences(SCREEN_HEIGHT)!!.toInt()/2
        nestedLayout.setOnScrollChangeListener { _: NestedScrollView?, _: Int, scrollY: Int, _: Int, _: Int ->
            if(scrollY<maxPos){
                bck.imageAlpha = 255 - (255*scrollY/maxPos)
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
        precentView = find(R.id.ac_message_present)
        calendar = find(R.id.ac_message_calendar)


        val dm = resources.displayMetrics
        headLayout.maxHeight = dm.heightPixels
        headLayout.minHeight = dm.heightPixels
    }

    override fun returnMessage(plan: Plan, totalSign: Int, state: String, isFinish: Boolean) {
        title.text = plan.title
        sign.text = "$totalSign 签到"
        message.text = if(isFinish) "今日完成" else "未签到"
        ing.text = state

        precentView.setMainProcess((plan.signDays*100.0/plan.targetTimes).toFloat())
        precentView.setMainText("进度")
        precentView.setLeftText("${plan.signDays}天")
        precentView.setRightText("${plan.targetTimes - plan.signDays}天")
        precentView.invalidate()
    }

    override fun setSignData(signMap: MutableMap<String, Sign>) {
        calendar.setData(signMap)

        calendar.setOnDateSelectListener(object : PlanCalendar.OnDateSelectListener{
            override fun onDateSelect(date: String) {
                toast(date)
            }

        })
    }

    override fun setBlurBck(bitmap: Bitmap) {
        blurBck.setImageBitmap(bitmap)
    }

    override fun setBck(bitmap: Bitmap) {
        bck.setImageBitmap(bitmap)
    }
}

