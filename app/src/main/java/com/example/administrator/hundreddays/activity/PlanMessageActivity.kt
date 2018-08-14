package com.example.administrator.hundreddays.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.administrator.hundreddays.R
import com.example.administrator.hundreddays.base.BaseActivity
import android.content.Intent
import android.util.Log
import android.widget.TextView
import com.example.administrator.hundreddays.bean.Plan
import com.example.administrator.hundreddays.bean.PlanIng
import com.example.administrator.hundreddays.constant.DATA
import com.example.administrator.hundreddays.presenter.PlanMessagePresenter
import com.example.administrator.hundreddays.view.PlanMessageView
import org.jetbrains.anko.find


class PlanMessageActivity : BaseActivity() ,PlanMessageView{


    private val TAG = "PlanMessageActivity"

    val presenter = PlanMessagePresenter(this,this)

    private lateinit var title:TextView
    private lateinit var message:TextView
    private lateinit var ing:TextView
    private lateinit var sign:TextView

    private var id = -1L

    override val contentView: Int get() = R.layout.activity_plan_message

    override fun init(savedInstanceState: Bundle?) {
        id = intent.getLongExtra(DATA,-1)
        presenter.initData(id)
        initView()

    }

    private fun initView() {
        title = find(R.id.ac_message_title)
        message = find(R.id.ac_message_msg)
        ing = find(R.id.ac_message_ing)
        sign = find(R.id.ac_message_sign)


    }

    override fun returnMessage(plan: Plan, totalSign: Int, state: String, isFinish: Boolean) {

    }
}