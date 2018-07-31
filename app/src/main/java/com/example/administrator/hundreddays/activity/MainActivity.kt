package com.example.administrator.hundreddays.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.administrator.hundreddays.R
import com.example.administrator.hundreddays.adapter.PlanAdapter
import com.example.administrator.hundreddays.base.BaseActivity
import com.example.administrator.hundreddays.base.BaseApplication
import com.example.administrator.hundreddays.bean.Plan
import com.example.administrator.hundreddays.bean.PlanIng
import com.example.administrator.hundreddays.presenter.MainPresenter
import com.example.administrator.hundreddays.sqlite.PlanDao
import com.example.administrator.hundreddays.sqlite.SignDao
import com.example.administrator.hundreddays.util.PagingScrollHelper
import com.example.administrator.hundreddays.util.getBitmapFromLocal
import com.example.administrator.hundreddays.util.getNowDateString
import com.example.administrator.hundreddays.view.MainView
import org.jetbrains.anko.find
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


class MainActivity : BaseActivity() , MainView {

    private val TAG = "MainActivity"

    private lateinit var title:TextView
    private lateinit var indicate:TextView
    private lateinit var bck:ImageView
    private lateinit var recycler:RecyclerView
    private lateinit var fab: FloatingActionButton

    private val mainPresenter:MainPresenter = MainPresenter(this,this)

    private lateinit var adapter: PlanAdapter
    private var scrollHelper: PagingScrollHelper = PagingScrollHelper()
    private var planIndex = 0
    private lateinit var planList:MutableList<PlanIng>

    override val contentView: Int get() = R.layout.activity_main

    override fun init(savedInstanceState: Bundle?) {
        mainPresenter.initData()
        initView()
        initEvent()
        checkList()
        title.setOnClickListener{ alert("你好","标题"){
            positiveButton("yes") { SignDao().alter() }
            negativeButton("no"){}
        }.show()}
    }


    private fun initEvent() {
        fab.setOnClickListener { startActivity<CreatePlanActivity>() }

        adapter.setOnItemClickListener { adapter, view, position ->
            startActivity<PlanMessageActivity>()
        }

        adapter.setOnItemChildClickListener { adapter, view, position ->
            removeIndex(position)
        }

        scrollHelper.setOnPageChangeListener(object : PagingScrollHelper.onPageChangeListener {
            override fun onPageChange(index: Int) {
                changeIndex(index)
            }
        })

    }

    private fun initView() {
        title = find(R.id.ac_main_title)
        indicate = find(R.id.ac_main_indicate)
        bck = find(R.id.ac_main_bck)
        recycler = find(R.id.ac_main_recycler)
        fab = find(R.id.ac_main_menu)

        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        LinearSnapHelper().attachToRecyclerView(recycler)
        adapter = PlanAdapter(planList)
        recycler.adapter = adapter
        scrollHelper.setUpRecycleView(recycler)

    }

    fun changeIndex(index:Int){
        if(planIndex == index){
            return
        }

        planIndex = index
        title.text = planList[index].plan?.title
        mainPresenter.getBlurBitmap(planList[index].plan?.imgUrl)

        indicate.text = "${index+1}/${planList.size}"
    }

    fun removeIndex(index: Int){
        toast("sign")
        //------------------------------------------
        val planIng = planList[index]
        planIng.insistentDay+=1
        planIng.lastSignDay = getNowDateString()
        mainPresenter.sign(planIng)
        //------------------------------------------
        planIng.isFinish = true
        adapter.notifyItemChanged(index)
    }

    fun checkList(){
        if(planList.isNotEmpty()) {
            changeIndex(0)
        }else{
            title.visibility = View.GONE
            indicate.visibility = View.GONE
            recycler.visibility = View.GONE
        }

    }

    override fun before() {

    }

    override fun success(data: MutableList<PlanIng>) {
        planList = data
    }

    override fun failure(error: Throwable) {
    }

    override fun after() {
    }

    override fun setBackgroud(bitmap: Bitmap) {
        bck.setImageBitmap(bitmap)
    }

    override fun setMessageDialog(str: String) {
        alert(str,"提醒"){
            positiveButton("yes") { dialog -> dialog.dismiss() }
        }.show()
    }
}