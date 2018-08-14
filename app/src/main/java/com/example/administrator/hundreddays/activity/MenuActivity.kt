package com.example.administrator.hundreddays.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.*
import com.example.administrator.hundreddays.R
import com.example.administrator.hundreddays.adapter.GridAdapter
import com.example.administrator.hundreddays.base.BarBaseActivity
import com.example.administrator.hundreddays.bean.GridBean
import org.jetbrains.anko.find

class MenuActivity : BarBaseActivity() {

    private val ACTIVITY = arrayOf<Class<*>>(CreatePlanActivity::class.java, CreatePlanActivity::class.java, CreatePlanActivity::class.java, CreatePlanActivity::class.java, CreatePlanActivity::class.java, CreatePlanActivity::class.java, CreatePlanActivity::class.java, CreatePlanActivity::class.java, CreatePlanActivity::class.java)
    private val TITLE = arrayOf("Animation", "MultipleItem", "Header/Footer", "PullToRefresh", "Section", "EmptyView", "DragAndSwipe", "ItemClick", "ExpandableItem")
    private val IMG = intArrayOf(R.drawable.usericon_0, R.drawable.usericon_0, R.drawable.usericon_0, R.drawable.usericon_0, R.drawable.usericon_0, R.drawable.usericon_0, R.drawable.usericon_0, R.drawable.usericon_0, R.drawable.usericon_0)


    private lateinit var recyclerView: RecyclerView

    private lateinit var list: ArrayList<GridBean>

    private lateinit var adapter: GridAdapter

    override val contentView: Int
        get() = R.layout.activity_menu

    override fun init(savedInstanceState: Bundle?) {
        recyclerView = find(R.id.ac_menu_recycler)

        initData()

        recyclerView.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        adapter = GridAdapter(list)
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener { adapter, view, position ->
            startActivity(Intent(this,ACTIVITY[position]))
            finish()
        }

        setTopLeftButton(object:OnClickListener{
            override fun onClick() {
                finish()
            }
        })

    }

    private fun initData() {
        list = ArrayList()
        for( i in 0 until TITLE.size){
            list.add(GridBean(TITLE[i],IMG[i]))
        }
    }

}
