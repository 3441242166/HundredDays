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

val CREATE_PLAN = "${Environment.getExternalStorageDirectory()}/HundredDays/temp.jpg"

//----------------------------------------文件读写--------------------------------------------------
/**
 * 向本地SD卡写图片
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
    val proj = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = context.contentResolver.query(contentUri, proj, null, null, null);
    if (cursor.moveToFirst()) {
        val column = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        path = cursor.getString(column)
    }
    cursor.close()
    Log.i(TAG, "getRealPathFromURI  $path")
    return path
}
//----------------------------------------文件读写--------------------------------------------------


//----------------------------------------图片压缩--------------------------------------------------
// 质量压缩法：
fun compressImage(image: Bitmap?, name: String): String? {
    val filepath = "$PRIVATE_PATH/$name"
    try {
        val baos = ByteArrayOutputStream()
        image?.compress(Bitmap.CompressFormat.JPEG, 100, baos)//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        var options = 100
        while (baos.toByteArray().size / 1024 > 2000) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset()//重置baos即清空baos
            options -= 20//每次都减少20
            image?.compress(Bitmap.CompressFormat.JPEG, options, baos)//这里压缩options%，把压缩后的数据存放到baos中
        }

        //压缩好后写入文件中
        val fos = FileOutputStream(filepath)
        fos.write(baos.toByteArray())
        fos.flush()
        fos.close()
        return filepath
    } catch (e: IOException) {
        e.printStackTrace()
        return ""
    }
}
// 质量压缩法：
fun compressImage(image: Bitmap?): Bitmap? {
    return try {
        val baos = ByteArrayOutputStream()
        image?.compress(Bitmap.CompressFormat.JPEG, 100, baos)//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        var options = 100
        while (baos.toByteArray().size / 1024 > 500) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset()//重置baos即清空baos
            options -= 10//每次都减少10
            image?.compress(Bitmap.CompressFormat.JPEG, options, baos)//这里压缩options%，把压缩后的数据存放到baos中

        }
        image
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}
//----------------------------------------图片压缩--------------------------------------------------
fun getBlurPath(path:String):String{
    return path+"blurNormal"
}

fun getMessageBlurPath(path:String):String{
    return path+"blurMax"
}

fun getPath(path:String):String{
    return "$PRIVATE_PATH/$path"
}