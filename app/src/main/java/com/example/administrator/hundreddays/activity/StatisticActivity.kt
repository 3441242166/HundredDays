package com.example.administrator.hundreddays.activity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.example.administrator.hundreddays.R
import com.example.administrator.hundreddays.presenter.StatisticPresenter
import com.example.administrator.hundreddays.view.StatisticView
import de.hdodenhof.circleimageview.CircleImageView

import org.jetbrains.anko.find

class StatisticActivity : AppCompatActivity(), StatisticView {
    private val TAG = "StatisticActivity"


    val presenter = StatisticPresenter(this,this)

    private lateinit var imgBck:ImageView
    private lateinit var imgHead:CircleImageView
    private lateinit var txName:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistic)

        initView()
        initEvent()
        presenter.init()
    }

    private fun initView() {
        val layout = find<ConstraintLayout>(R.id.ac_statistic_constrain)
        val dm = resources.displayMetrics
        layout.maxHeight = dm.heightPixels
        layout.minHeight = dm.heightPixels

        imgBck = find(R.id.ac_statistic_bck)
        imgHead = find(R.id.ac_statistic_head)
        txName = find(R.id.ac_statistic_name)
    }

    private fun initEvent() {

        imgHead.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            startActivityForResult(intent,0)
        }

        txName.setOnClickListener {
            MaterialDialog.Builder(this).title("测试")
                    .input("输入一些东西把","测试") { _, input ->
                        presenter.changeName(input.toString())
                        txName.text = input
                    }.show()
        }

    }

    override fun setBck(bitmap: Bitmap?) {
        imgBck.setImageBitmap(bitmap)
    }

    override fun setHead(bitmap: Bitmap?) {
        imgHead.setImageBitmap(bitmap)
    }

    override fun setName(name: String?) {
        txName.text = name
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.i(TAG,"$requestCode $resultCode $data")
        //获取到用户所选图片的Uri
        if (data == null || data.data == null) {
            Log.i(TAG,"onActivityResult  data is null")
            return
        }
        val uri = data.data
        presenter.saveHeadImage(uri)
        Glide.with(this).load(uri).into(imgHead)

    }
}
