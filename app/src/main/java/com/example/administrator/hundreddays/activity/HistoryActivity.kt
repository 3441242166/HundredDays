package com.example.administrator.hundreddays.activity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.Spinner
import android.widget.TextView
import com.example.administrator.hundreddays.R
import com.example.administrator.hundreddays.adapter.HistoryAdapter
import com.example.administrator.hundreddays.base.BaseActivity
import com.example.administrator.hundreddays.bean.History
import com.example.administrator.hundreddays.constant.DATA
import com.example.administrator.hundreddays.presenter.HistoryPresenter
import com.example.administrator.hundreddays.view.HistoryView

import kotlinx.android.synthetic.main.activity_history.*
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity

class HistoryActivity : BaseActivity() , HistoryView {
    private val TAG = "HistoryActivity"

    private val presenter:HistoryPresenter = HistoryPresenter(this,this)

    override val contentView: Int
        get() = R.layout.activity_history

    private lateinit var recycler:RecyclerView
    private lateinit var spinnerType: Spinner
    private lateinit var spinnerSort: Spinner
    private lateinit var spinner: Spinner

    private val adapter:HistoryAdapter = HistoryAdapter(this)

    override fun init(savedInstanceState: Bundle?) {

        initView()
        initEvent()
        presenter.initData()
    }

    private fun initEvent() {
        ac_history_barlayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val offset = Math.abs(verticalOffset)
            val total = appBarLayout.totalScrollRange

            Log.i(TAG,"total = " +total.toString())
            Log.i(TAG,"verticalOffset = " +offset.toString())

            if( offset == total){
                ac_history_spinner.visibility = View.INVISIBLE
            }else{
                ac_history_spinner.visibility = View.VISIBLE
                Log.i(TAG,"offset != total  ${(1 - offset / total).toFloat()}")
                ac_history_spinner.alpha = ((1 - offset / total).toFloat())
            }
        }

        adapter.setOnItemClickListener { _, _, position ->
            startActivity<PlanMessageActivity>(DATA to adapter.data[position].id)
        }

    }

    private fun initView() {
        setSupportActionBar(ac_plan_toolbar)

        spinner = find(R.id.ac_spinner_1)
        spinnerType = find(R.id.ac_spinner_2)
        spinnerSort = find(R.id.ac_spinner_3)
        recycler = find(R.id.ac_plan_recycler)

        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler.adapter = adapter
    }

    override fun setData(data: MutableList<History>) {
        adapter.setNewData(data)
    }


}
