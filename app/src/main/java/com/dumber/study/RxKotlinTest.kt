package com.dumber.study

import android.annotation.SuppressLint
import io.reactivex.rxjava3.core.Observable

class RxKotlinTest {

    fun test() {
        testJust()
    }

    @SuppressLint("CheckResult")
    fun testJust() {
        println("-- testJust start --")

        Observable.just("정건국", "정소은", "정숙희")
            .filter { it.startsWith("정") }
            .map { "_${it}_"}
            .doOnComplete { println("Completed") }
            .subscribe {
                println(it)
            }

        println("-- testJust finished --\n\n")
    }

}