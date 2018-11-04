package com.example.administrator.hundreddays.activity

import android.Manifest
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
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Switch
import com.bumptech.glide.Glide
import com.example.administrator.hundreddays.constant.*
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import java.util.*
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView


class CreatePlanActivity : BarBaseActivity() ,CreatePlanView {
    private val TAG = "CreatePlanTAG"

    private val presenter = CreatePlanPresenter(this,this)
    override val contentView: Int get() = R.layout.activity_creat_plan

    lateinit var txTitle: EditText
    lateinit var txDays:TextView
    lateinit var txFrequent:TextView
    lateinit var txRemind:TextView
    lateinit var img:ImageView
    lateinit var swRemind:Switch
    lateinit var recyclerView: RecyclerView

    var bckUri:Uri? = null
    var planDays:Int = 30
    var frequentDay:Int = 1

    lateinit var processDialog:MaterialDialog


    override fun init(savedInstanceState: Bundle?) {

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),0)
        }

        setTitle("创建新计划")
        initView()
        initEvent()
        presenter.init()
    }

    private fun initView(){
        android.R.anim.slide_out_right
        txTitle = find(R.id.ac_creat_title)
        txDays = find(R.id.ac_creat_day)
        txFrequent = find(R.id.ac_creat_frequence)
        txRemind = find(R.id.ac_creat_remindtime)
        img = find(R.id.ac_creat_img)
        swRemind = find(R.id.ac_creat_remind)
        recyclerView = find(R.id.ac_creat_recommend)

        processDialog = MaterialDialog.Builder(this).title("Ing....")
                .progress(true, 100, true)
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
                presenter.createPlan(txTitle.text.toString(),txRemind.text.toString(),frequentDay,planDays,bckUri)
            }
        })

        txDays.setOnClickListener{
            val ar: TypedArray =  resources.obtainTypedArray(R.array.days)
            MaterialDialog.Builder(this)
                    .title("选择坚持次数")
                    .content("不断挑战自己")
                    .items(R.array.days)
                    .positiveText("确定")
                    .widgetColor(Color.RED)//改变颜色
                    .itemsCallbackSingleChoice(planDays - 30) { _, _, _, _ ->
                        true//false 的时候没有选中样式
                    }
                    //点击确定后获取选中的下标数组
                    .onPositive { dialog, _ ->
                        txDays.text = ar.getString(dialog.selectedIndex)
                        planDays = dialog.selectedIndex + 30
                        dialog.dismiss()
                        ar.recycle()
                    }
                    .onNegative { _, _ ->
                        ar.recycle()
                    }
                    .show()
        }

        txFrequent.setOnClickListener{
            val ar: TypedArray =  resources.obtainTypedArray(R.array.frequence)
            MaterialDialog.Builder(this)
                    .title("选择执行频率")
                    .content("查出频率时间未签到计划失败")
                    .items(R.array.frequence)
                    .positiveText("确定")
                    .widgetColor(Color.RED)
                    .itemsCallbackSingleChoice(frequentDay - 1) { _, _, _, _ ->
                        true
                    }
                    .onPositive { dialog, _ ->
                        frequentDay = dialog.selectedIndex + 1
                        txFrequent.text = ar.getString(dialog.selectedIndex)
                        dialog.dismiss()
                        ar.recycle()
                    }
                    .onNegative { _, _ ->
                        ar.recycle()
                    }
                    .show()
        }

        img.setOnClickListener{
            presenter.openGallery()
        }

        txRemind.setOnClickListener{
            val now = Calendar.getInstance()

            TimePickerDialog.newInstance({ _, hourOfDay, minute, _ ->
                val text = "$hourOfDay : $minute"
                txRemind.text = text
            }, now.get(Calendar.HOUR), now.get(Calendar.MINUTE),true).show(fragmentManager,"")

        }

        swRemind.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                txRemind.visibility = View.VISIBLE
            }else{
                txRemind.visibility = View.GONE
            }
        }

    }

    override fun setImage(bitmap: Bitmap) {
        img.setImageBitmap(bitmap)
    }

    override fun before() {
        processDialog.show()
    }

    override fun success(data: String) {
        toast(data)
        val intent = Intent()
        setResult(CREATE_SUCCESS, intent)
        finish()
    }

    override fun failure(error: String) {
        toast(error)
    }

    override fun after() {
        processDialog.dismiss()
    }

    override fun startIntent(intent: Intent, code: Int) {
        startActivityForResult(intent,code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            GALLERY_CODE ->{
                //获取到用户所选图片的Uri
                if(data?.data == null){
                    Log.i(TAG,"data is null")
                    return
                }
                bckUri = data.data
                Glide.with(this).load(data.data).into(img)
            }
        }
    }

}
