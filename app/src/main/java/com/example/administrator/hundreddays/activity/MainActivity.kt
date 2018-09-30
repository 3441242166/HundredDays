package com.example.administrator.hundreddays.activity

import android.animation.AnimatorInflater
import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.widget.ImageView
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.administrator.hundreddays.R
import com.example.administrator.hundreddays.adapter.PlanAdapter
import com.example.administrator.hundreddays.base.BaseActivity
import com.example.administrator.hundreddays.bean.History
import com.example.administrator.hundreddays.constant.CREATE_SUCCESS
import com.example.administrator.hundreddays.constant.DATA
import com.example.administrator.hundreddays.presenter.MainPresenter
import com.example.administrator.hundreddays.util.PagingScrollHelper
import com.example.administrator.hundreddays.view.MainView
import com.joaquimley.faboptions.FabOptions
import org.jetbrains.anko.find
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import com.example.administrator.hundreddays.constant.CREATE_PLAN
import com.example.administrator.hundreddays.constant.PLAN_LIST
import org.jetbrains.anko.toast

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

        adapter.setOnItemClickListener { _, view, position ->
              startActivity<PlanMessageActivity>(DATA to adapter.data[position].id)
//            val intent = Intent(this, PlanMessageActivity::class.java)
//            val bundle = ActivityOptions.makeSceneTransitionAnimation(this,view.find(R.id.item_plan_image),"bck").toBundle()
//            intent.putExtra(DATA,adapter.data[position].id)
//            startActivity(intent,bundle)
        }

        adapter.setOnItemChildClickListener { _, _, position ->
            toast("sign")
            MaterialDialog.Builder(this).title("测试")
                    .input("输入一些东西把","测试") { _, input ->
                        mainPresenter.sign(position,input.toString())
                    }.show()
        }

        scrollHelper.setOnPageChangeListener(object : PagingScrollHelper.onPageChangeListener {
            override fun onPageChange(index: Int) {
                mainPresenter.changeIndex(index)
            }
        })

        title.setOnClickListener{
            //title.animate().translationX(50f)

            alert("你好","标题"){
            positiveButton("yes") {}
            negativeButton("no"){}
        }.show()}

        title.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.i(TAG,"s =$s  start=$start before=$before count=$count")
//                val animationSet = AnimationSet(true)
//                val alphaAnimation1 = AlphaAnimation(0f, 1f)
//                alphaAnimation1.duration = 200
//                animationSet.addAnimation(alphaAnimation1)
//                title.startAnimation(animationSet)
            }

            override fun afterTextChanged(s: Editable?) {

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
        //LinearSnapHelper().attachToRecyclerView(recycler)
        recycler.adapter = adapter
        scrollHelper.setUpRecycleView(recycler)

        fab.setFabColor(R.color.fabColor)
        fab.setBackgroundColor(this,0x00000000)

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
                startActivity<StatisticActivity>()
            }
            R.id.fab_other -> {
                startActivity<SettingActivity>()
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

    override fun setData(data: MutableList<History>) {
        adapter.setNewData(data)
    }

    override fun setMessage(title:String, index:String) {

        val animation = AnimatorInflater.loadAnimator(this,R.animator.slide_in_left)
        animation.setTarget(title)
        animation.start()

        this.title.text = title
        indicate.text = index
    }

    override fun setBackground(path: String) {
//        Glide.with(this)
//                .load(bitmap)
//                .transition(DrawableTransitionOptions()
//                        .crossFade())
//                .apply(RequestOptions()).into(bck)
        Glide.with(this)
                .load(path)
                .apply(RequestOptions()).into(bck)
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