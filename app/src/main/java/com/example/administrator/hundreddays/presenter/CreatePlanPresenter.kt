package com.example.administrator.hundreddays.presenter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.FutureTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.administrator.hundreddays.R
import com.example.administrator.hundreddays.base.BaseApplication
import com.example.administrator.hundreddays.bean.History
import com.example.administrator.hundreddays.bean.Plan
import com.example.administrator.hundreddays.constant.*
import com.example.administrator.hundreddays.util.*
import com.example.administrator.hundreddays.view.CreatePlanView
import io.realm.Realm
import org.jetbrains.anko.find
import java.io.File
import java.util.*

class CreatePlanPresenter(val view: CreatePlanView, private val context: Context,val realm: Realm? = Realm.getDefaultInstance()) {
    private val TAG = "CreatePlanPresenter"

    var path:String = ""

    fun init(){
        val bitmap = BitmapFactory.decodeStream(context.assets.open("bck.jpg"))
        view.setImage(bitmap)
    }

    /**
     *  创建计划 (标题 提醒时间 频率 目标天数 图片uri)
     *  向plan表 和 history表添加数据
     *  本地保存压缩后的背景图片 以及 压缩后背景图片的模糊版本
     */
    fun createPlan(title:String, remindTime:String, frequent:Int, targetDay:Int, imgUri: Uri?){
        if (TextUtils.isEmpty(title)) {
            view.failure(Throwable("标题不能为空"))
            return
        }
        if (imgUri == null) {
            view.failure(Throwable("请选择图片"))
            return
        }

        view.before()

        path = getNowString(DATETYPE.DATE_TIME)
        realm?.beginTransaction()

        val plan = realm?.createObject(Plan::class.java,UUID.randomUUID().toString())
        if(plan == null){
            realm?.commitTransaction()
            Log.i(TAG,"create plan error")
            return
        }
        plan.imgPath = getPath(path)
        plan.title = title
        plan.createDateTime = getNowString(DATETYPE.DATE_TIME)
        plan.remindTime = remindTime
        plan.targetDay = targetDay
        plan.frequentDay = frequent

        val history = realm?.createObject(History::class.java,UUID.randomUUID().toString())
        history?.plan = plan
        history?.keepDay = 0
        history?.state = PLAN_ING
        history?.lastSignDate = getAddDayString(getNowString(DATETYPE.DATE_DATE),-1)

        realm?.commitTransaction()

        Glide.with(context) // could be an issue!
                .asBitmap()
                .load(imgUri)
                .into(target)

    }

    private val target =object :  SimpleTarget<Bitmap>(getValueFromSharedPreferences(SCREEN_WIDTH)!!.toInt(),getValueFromSharedPreferences(SCREEN_HEIGHT)!!.toInt()){
        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            //view.setImage(resource)
            saveBitmapToLocal(path,resource)
            saveBitmapToLocal(getBlurPath(path),blurBitmap(context,resource,5f))
            saveBitmapToLocal(getMessageBlurPath(path),blurBitmap(context,resource,25f))
            view.after()
            view.success("创建成功")
        }
    }

    fun openSelectAvatarDialog(parentLayout:Int) {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_select_bck, null)
        val popupWindow = getPopupWindow(view)
        //设置点击事件
        val cameraTextView = view.find<TextView>(R.id.dialog_cmera)
        val selectAvatar = view.find<TextView>(R.id.dialog_photo)
        cameraTextView.setOnClickListener{
            takeCamera()
            popupWindow.dismiss()
        }

        selectAvatar.setOnClickListener{
            openGallery()
            popupWindow.dismiss()
        }

        val parent = LayoutInflater.from(context).inflate(parentLayout, null)
        //显示PopupWindow
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0)
    }

    private fun getPopupWindow(view: View): PopupWindow {
        val popupWindow = PopupWindow(context)
        popupWindow.contentView = view
        popupWindow.width = WindowManager.LayoutParams.MATCH_PARENT
        popupWindow.height = WindowManager.LayoutParams.WRAP_CONTENT
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        popupWindow.isFocusable = true
        popupWindow.isOutsideTouchable = true
        return popupWindow
    }

    private fun takeCamera() {
        //构建隐式Intent
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(File(CREATE_PLAN)))
        //调用系统相机
        view.startIntent(intent,CAMERA_CODE)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        view.startIntent(intent,GALLERY_CODE)
    }

}