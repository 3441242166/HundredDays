package com.example.administrator.hundreddays.presenter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
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
import com.example.administrator.hundreddays.R
import com.example.administrator.hundreddays.bean.Plan
import com.example.administrator.hundreddays.bean.PlanIng
import com.example.administrator.hundreddays.constant.CAMERA_CODE
import com.example.administrator.hundreddays.constant.GALLERY_CODE
import com.example.administrator.hundreddays.sqlite.IngDao
import com.example.administrator.hundreddays.sqlite.PlanDao
import com.example.administrator.hundreddays.util.*
import com.example.administrator.hundreddays.view.CreatePlanView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.find
import java.io.File

class CreatePlanPresenter(val view: CreatePlanView, private val context: Context) {
    private val TAG = "CreatePlanPresenter"

    fun createPlan(plan: Plan,bitmap: Bitmap?){
        if (TextUtils.isEmpty(plan.title)) {
            view.failure(Throwable("标题不能为空"))
            return
        }

        view.before()

        Observable.create<Long> {
            plan.imgPath = compressImage(bitmap, getNowDateTimeString())!!
            val index: Long = PlanDao().insert(plan)
            Log.i(TAG,getAddDayString(getNowDateString(),-1))
            IngDao().insert(PlanIng(index, getAddDayString(getNowDateString(),-1)))

            it.onNext(0)
        }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
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