package com.example.administrator.hundreddays.util

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