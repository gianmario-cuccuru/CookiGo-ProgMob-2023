package com.example.cookigo

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

fun<T> LiveData<T>.getOrAwaitValue() : T{

    var data: T? = null
    val latch = CountDownLatch(1)

    val observer = object : Observer<T>{
        override fun onChanged(value: T) {
            data = value
            this@getOrAwaitValue.removeObserver(this)
            latch.countDown()
        }

    }

    this.observeForever(observer)
    try {
        if (!latch.await(5, TimeUnit.SECONDS)){
            throw TimeoutException("Live Data never gets its value")
        }
    }finally {
        this.removeObserver(observer)
    }

    return data as T
}