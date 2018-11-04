package com.example.administrator.hundreddays.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.*
import com.example.administrator.hundreddays.R
import com.example.administrator.hundreddays.adapter.GridAdapter
import com.example.administrator.hundreddays.base.BaseActivity
import com.example.administrator.hundreddays.bean.GridBean
import org.jetbrains.anko.find

class MenuActivity : BaseActivity() {

    private val ACTIVITY = arrayOf<Class<*>>(MainActivity::class.java, PlanListActivity::class.java,
            CreatePlanActivity::class.java, StatisticActivity::class.java,
            SettingActivity::class.java, CreatePlanActivity::class.java,
            CreatePlanActivity::class.java, CreatePlanActivity::class.java)
    private val TITLE = arrayOf("PlanList", "History",
            "CreatePlan", "Statistic",
            "Setting", "Honor", "Nothing",
            "Nothing", "Nothing")
    private val IMG = arrayOf(R.drawable.usericon_0, R.drawable.usericon_1,
            R.drawable.usericon_2, R.drawable.usericon_3,
            R.drawable.usericon_4, R.drawable.usericon_5,
            R.drawable.usericon_6, R.drawable.usericon_7)
    private val COLOR = arrayOf(Color.parseColor("#f4658b"), Color.parseColor("#eb0303"),
            Color.parseColor("#0054ff"),  Color.parseColor("#ffc600"),
            Color.parseColor("#a8ede1"),  Color.parseColor("#facd89"),
            Color.parseColor("#cce5f6"),  Color.parseColor("#fe6a01"),
            Color.parseColor("#565656"),  Color.parseColor("#333333"),
            Color.parseColor("#f4658b"))


    private lateinit var recyclerView: RecyclerView


    private lateinit var list: ArrayList<GridBean>
    private lateinit var adapter: GridAdapter

    override val contentView: Int get() = R.layout.activity_menu

    override fun init(savedInstanceState: Bundle?) {

        initData()
        initView()

        adapter.setOnItemClickListener { adapter, view, position ->
            startActivity(Intent(this,ACTIVITY[position]))
        }

    }

    private fun initView() {
        recyclerView = find(R.id.ac_menu_recycler)

        recyclerView.layoutManager = GridLayoutManager(this, 2)
        adapter = GridAdapter(list)
        recyclerView.adapter = adapter
        recyclerView.isNestedScrollingEnabled = false
    }

    private fun initData() {
        list = ArrayList()
        for( i in 0 until TITLE.size-1){
            list.add(GridBean(TITLE[i],IMG[i],COLOR[i]))
        }
    }

}
