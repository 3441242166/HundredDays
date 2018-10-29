package com.example.administrator.hundreddays.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.example.administrator.hundreddays.base.BaseApplication
import android.provider.MediaStore
import android.util.Log
import java.io.*

private val TAG = "FileConvertUtil"

val PRIVATE_PATH = BaseApplication.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString()


fun getBlurPath(path:String):String{
    return path+"blurNormal"
}

fun getMessageBlurPath(path:String):String{
    return path+"blurMax"
}

fun getImagePath(path:String):String{
    return "$PRIVATE_PATH/$path"
}
//----------------------------------------文件读写--------------------------------------------------
/**
 * 向本地SD卡写图片
 */
fun saveBitmapToLocal(imgName: String, bitmap: Bitmap?): String {
    val file = File(getImagePath(imgName))
    try {
        val fos:OutputStream = file.outputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG,100,fos)
        fos.flush()
        fos.close()
    }catch (e:Exception){
        e.printStackTrace()
    }
    Log.i(TAG,"saveBitmapToLocal  "+file.toString())

    return file.toString()
}
/**
 * 通过指定路径获取Bitmap
 */
fun getBitmapFromLocal(imgPath: String?): Bitmap? {
    Log.i(TAG, "getBitmapFromLocal  $imgPath")
    return BitmapFactory.decodeFile(imgPath)
}
/**
 * 通过指定URI获取Bitmap
 */
fun getBitmapFromUri(context: Context,uri: Uri?): Bitmap? {
    if(uri == null){
        return null
    }
    return getBitmapFromLocal(getRealPathFromURI(context,uri))
}
/**
 * 获取指定URI的文件路径
 */
fun getRealPathFromURI(context: Context,contentUri:Uri):String{
    var path = ""
    val pro = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = context.contentResolver.query(contentUri, pro, null, null, null);
    if (cursor.moveToFirst()) {
        val column = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        path = cursor.getString(column)
    }
    cursor.close()
    Log.i(TAG, "getRealPathFromURI  $path")
    return path
}
//----------------------------------------文件读写--------------------------------------------------

