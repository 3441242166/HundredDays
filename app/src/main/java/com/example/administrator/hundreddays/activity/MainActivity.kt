package com.example.administrator.hundreddays.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.example.administrator.hundreddays.R
import com.example.administrator.hundreddays.adapter.PlanAdapter
import com.example.administrator.hundreddays.base.BaseActivity
import com.example.administrator.hundreddays.bean.History
import com.example.administrator.hundreddays.constant.*
import com.example.administrator.hundreddays.presenter.MainPresenter
import com.example.administrator.hundreddays.util.PagingScrollHelper
import com.example.administrator.hundreddays.view.MainView
import com.joaquimley.faboptions.FabOptions
import com.example.administrator.hundreddays.util.getBlurPath
import com.example.administrator.hundreddays.util.getValueFromSharedPreferences
import org.jetbrains.anko.*

class MainActivity : BaseActivity() , MainView,View.OnClickListener {
    private val TAG = "MainActivity"

    private lateinit var title:TextView
    private lateinit var preTitle:TextView
    private lateinit var nextTitle:TextView
    private lateinit var indicate:TextView

    private lateinit var bck:ImageView
    private lateinit var preBck:ImageView
    private lateinit var nextBck:ImageView

    private lateinit var recycler:RecyclerView
    private lateinit var fab: FabOptions

    private val mainPresenter:MainPresenter = MainPresenter(this,this)
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

    var startPos = 0f
    val maxPos = getValueFromSharedPreferences(SCREEN_WIDTH)!!.toInt()
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
            MaterialDialog.Builder(this).title("日记")
                    .input("写一些东西吧","") { _, input ->
                        mainPresenter.sign(position,input.toString())
                    }.show()
        }

        scrollHelper.setOnPageChangeListener(object : PagingScrollHelper.onPageChangeListener {
            override fun onPageChange(index: Int) {
                mainPresenter.changeIndex(index)
                startPos = 0f
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
//                Log.i(TAG,"s =$s  start=$start before=$before count=$count")
//                val animationSet = AnimationSet(true)
//                val alphaAnimation1 = AlphaAnimation(0f, 1f)
//                alphaAnimation1.duration = 200
//                animationSet.addAnimation(alphaAnimation1)
//                title.startAnimation(animationSet)
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                startPos +=dx
                val alpha = (255*startPos/maxPos)
                mainPresenter.changeAlpha(alpha)
                Log.i(TAG,"dx = $dx  dy = $dy startPos = $startPos  alpha = $alpha")
            }
        })
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
        fab = find(R.id.ac_main_menu)
        preBck.imageAlpha = 0
        nextBck.imageAlpha = 0

        bckAr= arrayOf(preBck,bck,nextBck)
        titleAr = arrayOf(preTitle,title,nextTitle)

        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

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

    override fun setMessage(pos:Int,name:String) {

//        val animation = AnimatorInflater.loadAnimator(this,R.animator.slide_in_left)
//        animation.setTarget(this.name)
//        animation.start()

//        title.animate()
//                .alpha(1f)
//                .interpolator = LinearInterpolator()

        //title.postDelayed({title.text = name},300)
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