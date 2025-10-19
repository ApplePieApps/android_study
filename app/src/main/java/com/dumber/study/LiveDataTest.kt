package com.dumber.study

import android.annotation.SuppressLint
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

class LiveDataTest {

    @SuppressLint("CheckResult")
    fun test(lifecycleOwner: LifecycleOwner) {
        val myData = MutableLiveData("Hello")
        myData.observe(lifecycleOwner) {
            println("myData Changed: $it")
        }
        Observable.timer(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                myData.value = "World"
            }
    }

}