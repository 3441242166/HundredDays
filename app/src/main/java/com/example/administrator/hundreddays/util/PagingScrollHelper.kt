package com.example.administrator.hundreddays.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View

class PagingScrollHelper {

    internal var mRecyclerView: RecyclerView? = null

    private val mOnScrollListener = MyOnScrollListener()

    private val mOnFlingListener = MyOnFlingListener()
    private var offsetY = 0
    private var offsetX = 0

    internal var startY = 0
    internal var startX = 0

    internal var mOrientation = ORIENTATION.HORIZONTAL

    internal var mAnimator: ValueAnimator? = null

    private val mOnTouchListener = MyOnTouchListener()

    private val pageIndex: Int
        get() {
            var p = 0
            if (mOrientation == ORIENTATION.VERTICAL) {
                p = offsetY / mRecyclerView!!.height
            } else {
                p = offsetX / mRecyclerView!!.width
            }
            return p
        }

    private val startPageIndex: Int
        get() {
            var p = 0
            if (mOrientation == ORIENTATION.VERTICAL) {
                p = startY / mRecyclerView!!.height
            } else {
                p = startX / mRecyclerView!!.width
            }
            return p
        }

    internal var mOnPageChangeListener: onPageChangeListener? = null


    internal enum class ORIENTATION {
        HORIZONTAL, VERTICAL, NULL
    }

    fun setUpRecycleView(recycleView: RecyclerView?) {
        if (recycleView == null) {
            throw IllegalArgumentException("recycleView must be not null")
        }
        mRecyclerView = recycleView
        //处理滑动
        recycleView.onFlingListener = mOnFlingListener
        //设置滚动监听，记录滚动的状态，和总的偏移量
        recycleView.setOnScrollListener(mOnScrollListener)
        //记录滚动开始的位置
        recycleView.setOnTouchListener(mOnTouchListener)
        //获取滚动的方向
        updateLayoutManger()
    }

    fun updateLayoutManger() {
        val layoutManager = mRecyclerView!!.layoutManager
        if (layoutManager != null) {
            if (layoutManager.canScrollVertically()) {
                mOrientation = ORIENTATION.VERTICAL
            } else if (layoutManager.canScrollHorizontally()) {
                mOrientation = ORIENTATION.HORIZONTAL
            } else {
                mOrientation = ORIENTATION.NULL
            }
            if (mAnimator != null) {
                mAnimator!!.cancel()
            }
            startX = 0
            startY = 0
            offsetX = 0
            offsetY = 0

        }

    }

    inner class MyOnFlingListener : RecyclerView.OnFlingListener() {

        override fun onFling(velocityX: Int, velocityY: Int): Boolean {
            if (mOrientation == ORIENTATION.NULL) {
                return false
            }
            //获取开始滚动时所在页面的index
            var p = startPageIndex

            //记录滚动开始和结束的位置
            var endPoint = 0
            var startPoint = 0

            //如果是垂直方向
            if (mOrientation == ORIENTATION.VERTICAL) {
                startPoint = offsetY

                if (velocityY < 0) {
                    p--
                } else if (velocityY > 0) {
                    p++
                }
                //更具不同的速度判断需要滚动的方向
                //注意，此处有一个技巧，就是当速度为0的时候就滚动会开始的页面，即实现页面复位
                endPoint = p * mRecyclerView!!.height

            } else {
                startPoint = offsetX
                if (velocityX < 0) {
                    p--
                } else if (velocityX > 0) {
                    p++
                }
                endPoint = p * mRecyclerView!!.width

            }
            if (endPoint < 0) {
                endPoint = 0
            }

            //使用动画处理滚动
            if (mAnimator == null) {
                mAnimator = ValueAnimator.ofInt(startPoint, endPoint)

                mAnimator!!.duration = 300
                mAnimator!!.addUpdateListener { animation ->
                    val nowPoint = animation.animatedValue as Int

                    if (mOrientation == ORIENTATION.VERTICAL) {
                        val dy = nowPoint - offsetY
                        //这里通过RecyclerView的scrollBy方法实现滚动。
                        mRecyclerView!!.scrollBy(0, dy)
                    } else {
                        val dx = nowPoint - offsetX
                        mRecyclerView!!.scrollBy(dx, 0)
                    }
                }
                mAnimator!!.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        //回调监听
                        if (null != mOnPageChangeListener) {
                            mOnPageChangeListener!!.onPageChange(pageIndex)
                        }
                    }
                })
            } else {
                mAnimator!!.cancel()
                mAnimator!!.setIntValues(startPoint, endPoint)
            }

            mAnimator!!.start()

            return true
        }
    }

    inner class MyOnScrollListener : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            //newState==0表示滚动停止，此时需要处理回滚
            if (newState == 0 && mOrientation != ORIENTATION.NULL) {
                val move: Boolean
                var vX = 0
                var vY = 0
                if (mOrientation == ORIENTATION.VERTICAL) {
                    val absY = Math.abs(offsetY - startY)
                    //如果滑动的距离超过屏幕的一半表示需要滑动到下一页
                    move = absY > recyclerView!!.height / 2
                    vY = 0

                    if (move) {
                        vY = if (offsetY - startY < 0) -1000 else 1000
                    }

                } else {
                    val absX = Math.abs(offsetX - startX)
                    move = absX > recyclerView!!.width / 2
                    if (move) {
                        vX = if (offsetX - startX < 0) -1000 else 1000
                    }

                }

                mOnFlingListener.onFling(vX, vY)

            }

        }

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            //滚动结束记录滚动的偏移量
            offsetY += dy
            offsetX += dx
        }
    }


    inner class MyOnTouchListener : View.OnTouchListener {

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            //手指按下的时候记录开始滚动的坐标
            if (event.action == MotionEvent.ACTION_DOWN) {
                startY = offsetY
                startX = offsetX
            }
            return false
        }

    }

    fun setOnPageChangeListener(listener: onPageChangeListener) {
        mOnPageChangeListener = listener
    }

    interface onPageChangeListener {
        fun onPageChange(index: Int)
    }

}