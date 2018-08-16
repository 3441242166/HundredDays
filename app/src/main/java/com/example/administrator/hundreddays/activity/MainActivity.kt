package com.example.administrator.hundreddays.activity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.administrator.hundreddays.R
import com.example.administrator.hundreddays.adapter.PlanAdapter
import com.example.administrator.hundreddays.base.BaseActivity
import com.example.administrator.hundreddays.bean.PlanIng
import com.example.administrator.hundreddays.constant.CREATE_SUCCESS
import com.example.administrator.hundreddays.constant.DATA
import com.example.administrator.hundreddays.presenter.MainPresenter
import com.example.administrator.hundreddays.sqlite.SignDao
import com.example.administrator.hundreddays.util.PagingScrollHelper
import com.example.administrator.hundreddays.view.MainView
import com.joaquimley.faboptions.FabOptions
import org.jetbrains.anko.find
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import com.example.administrator.hundreddays.constant.CREATE_PLAN
import com.example.administrator.hundreddays.constant.PLAN_LIST


class MainActivity : BaseActivity() , MainView,View.OnClickListener {
    private val TAG = "MainActivity"

    private lateinit var title:TextView
    private lateinit var indicate:TextView
    private lateinit var bck:ImageView
    private lateinit var recycler:RecyclerView
    private lateinit var fab: FabOptions

    private val mainPresenter:MainPresenter = MainPresenter(this,this)
    private val adapter: PlanAdapter = PlanAdapter(null,this)
    private var scrollHelper: PagingScrollHelper = PagingScrollHelper()

    override val contentView: Int get() = R.layout.activity_main

    override fun init(savedInstanceState: Bundle?) {
        initView()
        initEvent()
        mainPresenter.initData()
    }

    private fun initEvent() {
        fab.setOnClickListener(this)

        adapter.setOnItemClickListener { _, _, position ->
            startActivity<PlanMessageActivity>(DATA to adapter.data[position].id)
        }

        adapter.setOnItemChildClickListener { _, _, position ->
            mainPresenter.sign(position,"")
        }

        scrollHelper.setOnPageChangeListener(object : PagingScrollHelper.onPageChangeListener {
            override fun onPageChange(index: Int) {
                mainPresenter.changeIndex(index)
            }
        })

        title.setOnClickListener{ alert("你好","标题"){
            positiveButton("yes") { SignDao().alter() }
            negativeButton("no"){}
        }.show()}

    }

    private fun initView() {
        title = find(R.id.ac_main_title)
        indicate = find(R.id.ac_main_indicate)
        bck = find(R.id.ac_main_bck)
        recycler = find(R.id.ac_main_recycler)
        fab = find(R.id.ac_main_menu)

        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        LinearSnapHelper().attachToRecyclerView(recycler)
        recycler.adapter = adapter
        scrollHelper.setUpRecycleView(recycler)

        fab.setFabColor(R.color.fabColor)
        fab.setBackgroundColor(this,0x7e00b7ff)

    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_add ->{
                startActivityForResult(Intent(
                        this,
                        CreatePlanActivity::class.java
                ),CREATE_PLAN)
            }
            R.id.fab_setting ->{
                startActivityForResult(Intent(
                        this,
                        HistoryActivity::class.java
                ),PLAN_LIST)
            }
            R.id.fab_list -> {
                startActivity<SignActivity>()
            }
            R.id.fab_other -> {
                startActivity<StatisticActivity>()
            }
        }
    }

    override fun signSuccess(pos:Int){
        adapter.notifyItemChanged(pos)
    }

    override fun setVisibility(boolean: Boolean){
        if (boolean){
            title.visibility = View.VISIBLE
            indicate.visibility = View.VISIBLE
            recycler.visibility = View.VISIBLE
        }else{
            title.visibility = View.GONE
            indicate.visibility = View.GONE
            recycler.visibility = View.GONE
        }
    }

    override fun setData(data: MutableList<PlanIng>) {
        adapter.setNewData(data)
    }

    override fun setMessage(title:String, index:String) {
        this.title.text = title
        indicate.text = index
    }

    override fun setBackgroud(bitmap: Bitmap) {
        Glide.with(this).load(bitmap).transition(DrawableTransitionOptions().crossFade()).apply(RequestOptions()).into(bck)
    }

    override fun setMessageDialog(str: String) {
        alert(str,"提醒"){
            positiveButton("yes") { dialog -> dialog.dismiss() }
        }.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            CREATE_SUCCESS ->{
                mainPresenter.initData()
            }
        }
    }
}