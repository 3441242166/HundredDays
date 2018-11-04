package com.example.administrator.hundreddays.presenter

import android.content.Context
import android.graphics.Bitmap
import com.example.administrator.hundreddays.view.StatisticView
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.example.administrator.hundreddays.constant.HEAD_IMAGE
import com.example.administrator.hundreddays.constant.USER_NAME
import com.example.administrator.hundreddays.util.*


class StatisticPresenter(val context: Context,val view: StatisticView){
    private val TAG = "StatisticPresenter"


    fun init(){
        val path:String? = getValueFromSharedPreferences(HEAD_IMAGE)
        val bckBitmap: Bitmap?
        val headBitmap: Bitmap?
        Log.i(TAG,"path = $path")
        headBitmap = if(path.isNullOrEmpty()){
            Log.i(TAG,"headBitmap is null")
            BitmapFactory.decodeStream(context.assets.open("bck.jpg"))
        }else{
            getBitmapFromLocal(path)
        }
        bckBitmap = blurBitmap(context, headBitmap!!,50f)

        val name = getValueFromSharedPreferences(USER_NAME)
        //Log.i(TAG,name)
        if(!name.isNullOrEmpty()){
            view.setName(getValueFromSharedPreferences(USER_NAME))
        }else{
            view.setName("无名氏")
        }
        view.setBck(bckBitmap)
        view.setHead(headBitmap)

    }

    fun getStatisticData(){

    }

    fun saveHeadImage(uri: Uri?) {
        val bitmap = getBitmapFromUri(context,uri)
        val path = saveBitmapToLocal(HEAD_IMAGE, bitmap)
        saveToSharedPreferences(HEAD_IMAGE,path)
        view.setBck(blurBitmap(context, bitmap!!,15f))
    }

    fun changeName(toString: String) {
        saveToSharedPreferences(USER_NAME,toString)
    }

}