package com.example.administrator.hundreddays.base

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.*
import android.widget.FrameLayout
import android.widget.TextView
import com.example.administrator.hundreddays.R
import com.example.administrator.hundreddays.util.ActivityCollector
import org.jetbrains.anko.find
import android.widget.Button


abstract class BarBaseActivity : AppCompatActivity() {

    lateinit var toolbar: ConstraintLayout
    lateinit var viewContent: FrameLayout
    lateinit var tvTitle: TextView
    lateinit var btnRight: Button
    lateinit var btnLeft: Button
    private lateinit var onClickListenerTopLeft: OnClickListener
    private lateinit var onClickListenerTopRight: OnClickListener


    protected abstract val contentView: Int

    interface OnClickListener {
        fun onClick()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_base_bar)
        ActivityCollector.addActivity(this)

        toolbar = find(R.id.ac_base_toolbar)
        viewContent = find(R.id.ac_base_content)
        tvTitle = find(R.id.ac_base_title)
        btnRight = find(R.id.ac_base_right)
        btnLeft = find(R.id.ac_base_left)
        //初始化设置 Toolbar
        supportActionBar?.setDisplayShowTitleEnabled(false)

        //将继承 TopBarBaseActivity 的布局解析到 FrameLayout 里面
        LayoutInflater.from(this).inflate(contentView, viewContent)

        init(savedInstanceState)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
//        val params = toolbar.layoutParams
//        params.height = (toolbar.height + Math.ceil(((25 * resources.displayMetrics.density).toDouble()))).toInt()
//        toolbar.layoutParams = params
    }

    protected fun setTitle(title: String) {
        tvTitle.text = title
    }


    protected fun setTopLeftButton( onClickListener: OnClickListener,icon: Int = R.drawable.icon_exit) {
        btnLeft.visibility = View.VISIBLE

        btnLeft.setBackgroundResource(icon)
        val linearParams = btnLeft.layoutParams
        linearParams.height = 54
        linearParams.width = 54
        btnLeft.layoutParams = linearParams
        this.onClickListenerTopLeft = onClickListener
        btnLeft.setOnClickListener { onClickListenerTopLeft.onClick() }
    }

    protected fun setTopRightButton(onClickListener: OnClickListener,icon: Int = R.drawable.icon_certain) {
        btnRight.visibility = View.VISIBLE

        btnRight.setBackgroundResource(icon)
        val linearParams = btnRight.layoutParams
        linearParams.height = 54
        linearParams.width = 54
        btnRight.layoutParams = linearParams

        this.onClickListenerTopRight = onClickListener
        btnRight.setOnClickListener { onClickListenerTopRight.onClick() }
    }

    protected abstract fun init(savedInstanceState: Bundle?)

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }
}
