package com.example.administrator.hundreddays.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.AbsListView.OnScrollListener.*
import android.widget.ImageView
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.example.administrator.hundreddays.R
import com.example.administrator.hundreddays.adapter.PlanAdapter
import com.example.administrator.hundreddays.base.BaseActivity
import com.example.administrator.hundreddays.bean.Plan
import com.example.administrator.hundreddays.constant.*
import com.example.administrator.hundreddays.presenter.MainPresenter
import com.example.administrator.hundreddays.util.PagingScrollHelper
import com.example.administrator.hundreddays.view.MainView
import com.joaquimley.faboptions.FabOptions
import com.example.administrator.hundreddays.util.getValueFromSharedPreferences
import org.jetbrains.anko.*
import javax.security.auth.login.LoginException

class MainActivity : BaseActivity() , MainView {
    private val TAG = "MainActivity"
    private val mainPresenter:MainPresenter = MainPresenter(this,this)

    private lateinit var title:TextView
    private lateinit var preTitle:TextView
    private lateinit var nextTitle:TextView
    private lateinit var indicate:TextView
    private lateinit var bck:ImageView
    private lateinit var preBck:ImageView
    private lateinit var nextBck:ImageView
    private lateinit var recycler:RecyclerView
    private lateinit var fab: ImageView

    private val adapter: PlanAdapter = PlanAdapter(null,this)
    private var scrollHelper: PagingScrollHelper = PagingScrollHelper()
    private lateinit var bckAr:Array<ImageView>
    private lateinit var titleAr:Array<TextView>

    override val contentView: Int get() = R.layout.activity_main

    override fun init(savedInstanceState: Bundle?) {
        initView()
        initEvent()
        mainPresenter.initData()
    }

    var startPos = 0
    private fun initEvent() {
        fab.setOnClickListener {
            finish()
        }

        adapter.setOnItemClickListener { _, view, position ->
              startActivity<PlanMessageActivity>(DATA to adapter.data[position].id , IMAGE_PATH to adapter.data[position].imgPath)
//            val intent = Intent(this, PlanMessageActivity::class.java)
//            val bundle = ActivityOptions.makeSceneTransitionAnimation(this,view.find(R.id.item_plan_image),"bck").toBundle()
//            intent.putExtra(DATA,adapter.data[position].id)
//            startActivity(intent,bundle)
        }

        adapter.setOnItemChildClickListener { _, view, _ ->
            MaterialDialog.Builder(this).title("日记")
                    .input("写一些东西吧","") { _, input ->
                        mainPresenter.sign(input.toString())
                    }.show()
        }

        scrollHelper.setOnPageChangeListener(object : PagingScrollHelper.OnPageChangeListener {
            override fun onPageChange(index: Int) {
                mainPresenter.changeIndex(index)
                startPos = 0
            }
        })

        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
               // Log.i(TAG, " sumPos = $startPos  index = $index")
                startPos += dx
                val alpha = 255 * startPos.toFloat() / recycler.width
                //Log.i(TAG, " alpha = $alpha recycler.width = ${recycler.width}")
                mainPresenter.changeAlpha(alpha)

            }
        })

        recycler.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            Log.i(TAG, "onScrollChange: scrollX = $scrollX  oldScrollX = $oldScrollX")
        }
    }

    private fun initView() {
        title = find(R.id.ac_main_title)
        preTitle = find(R.id.ac_main_title_pre)
        nextTitle = find(R.id.ac_main_title_next)
        indicate = find(R.id.ac_main_indicate)
        bck = find(R.id.ac_main_bck)
        preBck = find(R.id.ac_main_bck_pre)
        nextBck = find(R.id.ac_main_bck_next)
        recycler = find(R.id.ac_main_recycler)
        fab = find(R.id.ac_main_back)

        preBck.imageAlpha = 0
        nextBck.imageAlpha = 0
        preTitle.textColor  = Color.argb(0, 255, 255, 255)
        nextTitle.textColor  = Color.argb(0, 255, 255, 255)
        bckAr= arrayOf(preBck,bck,nextBck)
        titleAr = arrayOf(preTitle,title,nextTitle)

        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler.adapter = adapter
        scrollHelper.setUpRecycleView(recycler)
        //PagerSnapHelper().attachToRecyclerView(recycler)
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

    override fun setData(data: MutableList<Plan>) {
        adapter.setNewData(data)
    }

    override fun setMessage(pos:Int,name:String) {
        titleAr[pos].text = name
    }

    override fun setBackground(pos:Int,path: String) {
        Glide.with(this)
                .load(path)
                .into(bckAr[pos])
    }

    override fun setMessageDialog(str: String) {
        alert(str,"提醒"){
            positiveButton("yes") { dialog -> dialog.dismiss() }
        }.show()
    }

    override fun setViewAlpha(index: Int, alpha: Float) {
        bckAr[index].imageAlpha  = alpha.toInt()
    }

    override fun setTextViewAlpha(index: Int, alpha: Float) {
        titleAr[index].textColor  = Color.argb(alpha.toInt(), 255, 255, 255)
    }

    override fun setIndex(index: String) {
        indicate.text = index
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            CREATE_SUCCESS ->{
                mainPresenter.initData()
            }
        }
    }
}