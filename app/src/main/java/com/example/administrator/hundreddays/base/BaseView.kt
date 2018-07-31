package com.example.administrator.hundreddays.base

interface BaseView<T> {
    fun before()
    fun success(data:T)
    fun failure(error:Throwable)
    fun after()
}