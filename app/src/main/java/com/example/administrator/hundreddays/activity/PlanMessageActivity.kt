package com.example.administrator.hundreddays.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.administrator.hundreddays.R
import com.example.administrator.hundreddays.base.BaseActivity
import android.content.Intent
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.TransitionOptions
import com.bumptech.glide.load.resource.bitmap.DrawableTransformation
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.administrator.hundreddays.bean.Plan
import com.example.administrator.hundreddays.bean.PlanIng
import com.example.administrator.hundreddays.constant.DATA
import com.example.administrator.hundreddays.presenter.PlanMessagePresenter
import com.example.administrator.hundreddays.view.PlanMessageView
import org.jetbrains.anko.find
import java.lang.System.load


class PlanMessageActivity : BaseActivity() ,PlanMessageView{


    private val TAG = "PlanMessageActivity"

    val presenter = PlanMessagePresenter(this,this)

    private lateinit var title:TextView
    private lateinit var message:TextView
    private lateinit var ing:TextView
    private lateinit var sign:TextView
    private lateinit var bck:ImageView

    private var id = -1L

    override val contentView: Int get() = R.layout.activity_plan_message

    override fun init(savedInstanceState: Bundle?) {
        id = intent.getLongExtra(DATA,-1)
        initView()

        presenter.initData(id)

    }

    private fun initView() {
        title = find(R.id.ac_message_title)
        message = find(R.id.ac_message_msg)
        ing = find(R.id.ac_message_ing)
        sign = find(R.id.ac_message_sign)
        bck = find(R.id.ac_message_bck)

    }

    override fun returnMessage(plan: Plan, totalSign: Int, state: Boolean, isFinish: Boolean) {
        title.text = plan.title
        Glide.with(this).load(plan.imgPath).transition(DrawableTransitionOptions().crossFade()).apply(RequestOptions()).into(bck)

        sign.text = "$totalSign 签到"
        message.text = if(state) "今日完成" else "未签到"
        ing.text = if(isFinish) "ing..." else "finish..."

    }
}

