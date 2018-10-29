package com.example.administrator.hundreddays.activity

import android.content.res.TypedArray
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Spinner
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.example.administrator.hundreddays.R
import com.example.administrator.hundreddays.adapter.HistoryAdapter
import com.example.administrator.hundreddays.base.BaseActivity
import com.example.administrator.hundreddays.bean.Plan
import com.example.administrator.hundreddays.constant.DATA
import com.example.administrator.hundreddays.presenter.HistoryPresenter
import com.example.administrator.hundreddays.view.HistoryView

import kotlinx.android.synthetic.main.activity_plan_list.*
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity

class PlanListActivity : BaseActivity() , HistoryView {
    private val TAG = "PlanListActivity"

    private val presenter:HistoryPresenter = HistoryPresenter(this,this)

    override val contentView: Int
        get() = R.layout.activity_plan_list

    private lateinit var recycler:RecyclerView
    private lateinit var txDate: TextView
    private lateinit var btSort: TextView
    private lateinit var fab: FloatingActionButton

    private val adapter:HistoryAdapter = HistoryAdapter(this)

    override fun init(savedInstanceState: Bundle?) {

        initView()
        initEvent()
        presenter.initData()
    }

    private fun initEvent() {
        adapter.setOnItemClickListener { _, _, position ->
            startActivity<PlanMessageActivity>(DATA to adapter.data[position].id)
        }

        btSort.setOnClickListener{
            val ar: TypedArray =  resources.obtainTypedArray(R.array.sort)
            MaterialDialog.Builder(this)
                    .title("选择排序方式")
                    .items(R.array.sort)
                    .positiveText("确定")
                    .negativeText("取消")
                    .widgetColor(Color.RED)//改变颜色
                    .itemsCallbackSingleChoice(0) { _, _, _, _ ->
                        true//false 的时候没有选中样式
                    }
                    //点击确定后获取选中的下标数组
                    .onPositive { dialog, _ ->
                        btSort.text = ar.getString(dialog.selectedIndex)
                        dialog.dismiss()
                        ar.recycle()
                    }
                    .show()
        }

        fab.setOnClickListener {
            startActivity<CreatePlanActivity>()
            finish()
        }
    }

    private fun initView() {
        txDate = find(R.id.ac_list_date)
        recycler = find(R.id.ac_list_recycler)
        btSort = find(R.id.ac_list_button)
        fab = find(R.id.ac_list_fab)

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }

    override fun setData(data: MutableList<Plan>) {
        adapter.setNewData(data)
    }


}
