package com.example.administrator.hundreddays.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.example.administrator.hundreddays.R
import com.example.administrator.hundreddays.base.BarBaseActivity
import com.example.administrator.hundreddays.presenter.CreatePlanPresenter
import com.example.administrator.hundreddays.view.CreatePlanView
import org.jetbrains.anko.find
import org.jetbrains.anko.toast
import android.content.Intent
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Color
import com.bumptech.glide.Glide
import com.example.administrator.hundreddays.bean.Plan
import com.example.administrator.hundreddays.constant.CAMERA_CODE
import com.example.administrator.hundreddays.constant.GALLERY_CODE
import com.example.administrator.hundreddays.sqlite.PlanDao
import com.example.administrator.hundreddays.util.getBitmapFromLocal
import com.example.administrator.hundreddays.util.getBitmapFromUri
import com.example.administrator.hundreddays.util.getNowDateString
import com.example.administrator.hundreddays.util.getPathFromUri
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import java.util.*


class CreatePlanActivity : BarBaseActivity() ,CreatePlanView {

    private val presenter = CreatePlanPresenter(this,this)

    lateinit var txTitle:TextView
    lateinit var txDays:TextView
    lateinit var txFrequence:TextView
    lateinit var txRemind:TextView
    lateinit var img:ImageView

    var bitmap:Bitmap? = null
    var bitmapPath:String =""
    var planDays:Int = 30
    var frequentDay:Int = 1

    lateinit var processDialog:MaterialDialog

    override val contentView: Int
        get() = R.layout.activity_creat_plan


    override fun init(savedInstanceState: Bundle?) {
        setTitle("创建新计划")
        initView()
        initEvent()
    }

    private fun initView(){
        txTitle = find(R.id.ac_creat_title)
        txDays = find(R.id.ac_creat_day)
        txFrequence = find(R.id.ac_creat_frequence)
        txRemind = find(R.id.ac_creat_remind)
        img = find(R.id.ac_creat_img)

        processDialog = MaterialDialog.Builder(this).title("")
                .progress(false, 100, true)
                .build()
    }

    private fun initEvent(){
        setTopLeftButton(object:OnClickListener{
            override fun onClick() {
                finish()
            }
        })

        setTopRightButton(object :OnClickListener{
            override fun onClick() {
                packagePlan()
            }
        })

        txTitle.setOnClickListener{
            MaterialDialog.Builder(this).title("计划名称")
                    .input("输入标题",txTitle.text) { _, input ->
                        txTitle.text = input
                    }.show()
        }

        txDays.setOnClickListener{
            val ar: TypedArray =  resources.obtainTypedArray(R.array.days)
            MaterialDialog.Builder(this)
                    .title("选择持续时长")
                    .content("不断挑战自己")
                    .items(R.array.days)
                    .positiveText("确定")
                    .widgetColor(Color.RED)//改变颜色
                    .itemsCallbackSingleChoice(0) { _, _, _, _ ->
                        true//false 的时候没有选中样式
                    }
                    //点击确定后获取选中的下标数组
                    .onPositive { dialog, _ ->
                        txDays.text = ar.getString(dialog.selectedIndex)
                        planDays = dialog.selectedIndex + 30
                        dialog.dismiss()
                    }
                    .show()
        }

        txFrequence.setOnClickListener{
            val ar: TypedArray =  resources.obtainTypedArray(R.array.frequence)
            MaterialDialog.Builder(this)
                    .title("选择执行频率")
                    .content("查出频率时间未签到计划失败")
                    .items(R.array.frequence)
                    .positiveText("确定")
                    .widgetColor(Color.RED)
                    .itemsCallbackSingleChoice(0) { _, _, _, _ ->
                        true
                    }
                    .onPositive { dialog, _ ->
                        frequentDay = dialog.selectedIndex + 1
                        txFrequence.text = ar.getString(dialog.selectedIndex)
                        dialog.dismiss()
                    }
                    .show()
        }

        img.setOnClickListener{
            presenter.openSelectAvatarDialog(contentView)
        }

        txRemind.setOnClickListener{
            val now = Calendar.getInstance()

            TimePickerDialog.newInstance({ _, hourOfDay, minute, _ ->
                val text = "$hourOfDay : $minute"
                txRemind.text = text
            }, now.get(Calendar.HOUR), now.get(Calendar.MINUTE),true).show(fragmentManager,"")

        }

    }

    fun packagePlan(){
        val plan = Plan(null,txTitle.text.toString(),txRemind.text.toString(),bitmapPath,getNowDateString(),frequentDay,planDays)
        presenter.createPlan(plan,bitmap)
    }

    override fun before() {
        processDialog.show()
    }

    override fun success(data: String) {
        toast(data)
        finish()
    }

    override fun failure(error: Throwable) {
        toast(error.toString())
    }

    override fun after() {
        processDialog.dismiss()
    }

    override fun startIntent(intent: Intent, code: Int) {
        startActivityForResult(intent,code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            CAMERA_CODE ->{
                val extras = data?.extras
                //获得拍的照片
                bitmap = extras?.getParcelable("data")
                Glide.with(this).load(bitmap).into(img)
            }
            GALLERY_CODE ->{
                //获取到用户所选图片的Uri
                if(data?.data == null){
                    return
                }
                val uri = data.data
                bitmap = getBitmapFromUri(uri)

                Glide.with(this).load(bitmap).into(img)
            }
            else -> {

            }

        }
    }

}
