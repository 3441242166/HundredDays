package com.example.administrator.hundreddays.presenter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.provider.MediaStore
import android.text.TextUtils
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
import com.example.administrator.hundreddays.util.getNowDateTimeString
import com.example.administrator.hundreddays.util.saveBitmapToLocal
import com.example.administrator.hundreddays.view.CreatePlanView
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.find
import java.util.*

class CreatePlanPresenter(val view: CreatePlanView, private val context: Context) {

    fun createPlan(plan: Plan,bitmap: Bitmap?){
        view.before()

        Observable.create<Long> {
            var code = -1L
            var index: Long

            while (true) {
                if (TextUtils.isEmpty(plan.title)) {
                    code = 1L
                    break
                }
                plan.imgUrl = saveBitmapToLocal(getNowDateTimeString(), bitmap)
                if (TextUtils.isEmpty(plan.imgUrl) || bitmap==null) {
                    code = 2L
                    break
                }
                index = PlanDao().insert(plan)
                if (index == -1L ) {
                    code = 3L
                }
                IngDao().insert(PlanIng(index))
                break
            }

            it.onNext(code)

        }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    view.after()
                    if(check(it)){
                        view.success("创建成功")
                    }
                }

        view.after()
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
        //调用系统相机
        view.startIntent(intent,CAMERA_CODE)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        view.startIntent(intent,GALLERY_CODE)
    }

    private fun check(code: Long?):Boolean{
        when (code) {
            1L ->
                view.failure(Throwable("标题不能为空"))
            2L ->
                view.failure(Throwable("请选择一张背景图片"))
            3L ->
                view.failure(Throwable("创建计划失败"))
            else ->
                return true
        }
        return false
    }
}