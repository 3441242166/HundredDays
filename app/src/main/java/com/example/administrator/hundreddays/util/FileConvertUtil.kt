package com.example.administrator.hundreddays.util

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.example.administrator.hundreddays.base.BaseApplication
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import android.provider.MediaStore
import android.util.Log
import java.io.IOException


private val TAG = "FileConvertUtil"

val PRIVATE_PATH = BaseApplication.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString()


/**
 * 向本地SD卡写网络图片
 *
 * @param bitmap
 */
fun saveBitmapToLocal(imgName: String, bitmap: Bitmap?): String {

    val file = File("$PRIVATE_PATH/$imgName")
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

fun getBitmapFromLocal(imgPath: String?): Bitmap? {
    Log.i(TAG, "getBitmapFromLocal  $imgPath")
    return BitmapFactory.decodeFile(imgPath)
}

fun getPathFromUri(uri: Uri?):String{
    val filePath: String
    //URI的scheme直接就是file://.....
    if ("file" == uri?.scheme) {
        //直接调用getPath方法就可以了
        filePath = uri.path
    } else {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = BaseApplication.context.contentResolver
                .query(uri, filePathColumn, null, null, null)
        cursor!!.moveToFirst()
        val path = cursor.getColumnIndex(filePathColumn[0])
        filePath = cursor.getString(path)
        cursor.close()
    }

    Log.i(TAG, "getPathFromUri  $filePath")
    return filePath
}


fun getBitmapFromUri(uri: Uri?): Bitmap? {
    return BitmapFactory.decodeFile(getPathFromUri(uri))
}