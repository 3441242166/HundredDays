package com.example.administrator.hundreddays.activity

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import com.example.administrator.hundreddays.R
import com.example.administrator.hundreddays.base.BarBaseActivity
import org.jetbrains.anko.find

class SignActivity : BarBaseActivity() {


    lateinit var recyclerView: RecyclerView

    override val contentView: Int
        get() = R.layout.activity_sign

    override fun init(savedInstanceState: Bundle?) {
        recyclerView = find(R.id.ac_sign_recycler)

    }

}
