package com.example.administrator.myview

import android.content.Context
import android.graphics.Color
import android.media.Image
import android.os.Parcel
import android.os.Parcelable
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import com.example.administrator.hundreddays.R
import com.example.administrator.hundreddays.adapter.GridAdapter
import com.example.administrator.hundreddays.bean.PlanIng
import com.example.administrator.hundreddays.sqlite.IngDao
import com.example.administrator.hundreddays.sqlite.PlanDao
import com.example.administrator.hundreddays.util.compressImage
import com.example.administrator.hundreddays.util.getAddDayString
import com.example.administrator.hundreddays.util.getNowDateString
import com.example.administrator.hundreddays.util.getNowDateTimeString
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.find
import org.w3c.dom.Text
import java.util.*

class MyCalendar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {

    lateinit var imgBack:ImageView
    lateinit var imgNext:ImageView
    lateinit var title:TextView
    lateinit var recycler:RecyclerView

    val curDate:Calendar = Calendar.getInstance()

    init {
        bindView()
        bindEvent()
        renderCalendar()
    }

    private fun bindEvent() {

        imgBack.setOnClickListener {
            curDate.add(Calendar.MONTH,-1)
            renderCalendar()
        }

        imgNext.setOnClickListener {
            curDate.add(Calendar.MONTH,1)
            renderCalendar()
        }

    }

    private fun renderCalendar() {
        val cells = arrayListOf<CalendarBean>()
        val calendar = curDate.clone() as Calendar
        calendar.set(Calendar.DAY_OF_MONTH,1)
        var prevDays = calendar.get(Calendar.DAY_OF_WEEK)-1
        calendar.add(Calendar.DAY_OF_MONTH, -prevDays)

        val max = 6*7

        while (cells.size <max){
            cells.add(CalendarBean(calendar.time,true))
            calendar.add(Calendar.DAY_OF_MONTH,1)
        }

        recycler.layoutManager = StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.VERTICAL)
        recycler.adapter = CalendarAdapter(cells)

    }

    private fun bindView() {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.view_calendar,this)

        imgBack = find(R.id.view_calendar_back)
        imgNext = find(R.id.view_calendar_next)
        title = find(R.id.view_calendar_date)
        recycler = find(R.id.view_calendar_recycler)


    }

    private fun switch(amount:Int){
        Observable.create<Long> {
            curDate.add(Calendar.MONTH,amount)
            renderCalendar()
            it.onNext(0)
        }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {

                }
    }
}