package com.dumber.study.rxkotlin

import android.annotation.SuppressLint
import com.google.gson.Gson
import com.google.gson.JsonParser
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit

class RxKotlinTest {

    fun test() {
//        testJust()
//        testCustomObservable()
//        testToList()
//        testZip()
//        testMerge()
//        testCombineLatest()
//        testDebounce()
//        testDistinct()
        testSample()
    }

    /*
        기본적인 Observable 코드 예제
     */
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


    /*
        Observable과 URL, Gson을 이용해서 네트워크로 항목들을 가져와서 항목들을 표시하는 코드
        Observable.create로 직접 emit
     */
    data class Item(val userId: Int, val id: Int, val title: String, val completed: Boolean) {
        override fun toString(): String {
            return "[$id/$userId] $title ($completed)"
        }
    }
    @SuppressLint("CheckResult")
    fun testCustomObservable() {
        Observable.create { emitter ->
            val url = URL("https://jsonplaceholder.typicode.com/todos")
            val connection = url.openConnection() as HttpURLConnection
            try {
                connection.requestMethod = "GET"
                connection.connectTimeout = 10000
                connection.readTimeout = 20000
                connection.connect()
                val jsonArray = connection.inputStream.bufferedReader().use {
                    return@use JsonParser.parseString(it.readText()).asJsonArray
                }
                val gson = Gson()
                jsonArray.forEach {
                    emitter.onNext(gson.fromJson(it, Item::class.java))
                }
                emitter.onComplete()
            } catch (e: Throwable) {
                emitter.onError(e)
            } finally {
                connection.disconnect()
            }
        }
            .toList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .onErrorComplete { println("error: $it"); true }
            .subscribe {
                println(it)
            }
    }

    /*
        Observable을 이용해서 아이템을 리스트로 만들어 보는 예제
     */
    @SuppressLint("CheckResult")
    fun testToList() {
        Observable.just(1, 2, 3)
            .toList()
            .subscribe {
                println(it)
            }
    }

    /*
        zip은 2개의 Observable을 조합해서 하나의 Observable을 만든다.
        각 Observable의 같은 index의 항목이 조합된다.
        a, b, c, d
        1, 2, 3
        에서 d는 짝이 맞지 않아 무시됨
     */
    @SuppressLint("CheckResult")
    fun testZip() {
        Observable.zip(
            Observable.just("a", "b", "c", "d")
            , Observable.just(1, 2, 3)
        ) { a, b ->
            a + b
        }
            .doOnComplete { println("Completed") }
            .subscribe {
                println("zipped: $it")
            }

    }

    /*
        merge 함수는 두 개의 Observable을 병합한다.
        두 개 중 어디서든 next가 들어오는 순서대로 항목을 전달한다.
     */
    @SuppressLint("CheckResult")
    fun testMerge() {
        val o1 = Observable.create { emitter ->
            emitter.onNext(1)
            Thread.sleep(100)
            emitter.onNext(2)
            Thread.sleep(300)
            emitter.onNext(3)
        }.subscribeOn(Schedulers.io())
        val o2 = Observable.create { emitter ->
            Thread.sleep(10)
            emitter.onNext("a")
            Thread.sleep(20)
            emitter.onNext("b")
            Thread.sleep(500)
            emitter.onNext("c")
        }.subscribeOn(Schedulers.io())
        Observable
            .merge(o1, o2)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                println("next: $it")
            }
    }

    /*
        combineLatest는 두 Observable을 조합한다.
        어느 한 Observable에서 onNext가 emit되면 두 Obserbale에서 emit되었던 마지막 항목을 조합한다.
        1,  2,    3
         a,b,          c
        -> a1, b1, b2, b3, c3
     */
    @SuppressLint("CheckResult")
    fun testCombineLatest() {
        val o1 = Observable.create { emitter ->
            emitter.onNext(1)
            Thread.sleep(100)
            emitter.onNext(2)
            Thread.sleep(300)
            emitter.onNext(3)
        }.subscribeOn(Schedulers.io())
        val o2 = Observable.create { emitter ->
            Thread.sleep(10)
            emitter.onNext("a")
            Thread.sleep(20)
            emitter.onNext("b")
            Thread.sleep(500)
            emitter.onNext("c")
        }.subscribeOn(Schedulers.io())

        Observable.combineLatest(o1, o2) { a, b -> b + a }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                println("next: $it")
            }
    }

    /*
        debounce는 아이템이 발행되고 일정 시간이 지나야 해당 항목이 발행되고
        그 시간 안에 다른 항목이 발행되면 대기하던 항목은 무시되고 새로운 항목이 대기하게 된다.
        예제에서는 150ms 시간을 주었기 때문에
        1과 3은 무시되고 2와 4만 발행된다.
     */
    @SuppressLint("CheckResult")
    fun testDebounce() {
        Observable.create { emitter ->
            emitter.onNext(1)
            Thread.sleep(100)
            emitter.onNext(2)
            Thread.sleep(200)
            emitter.onNext(3)
            Thread.sleep(100)
            emitter.onNext(4)
        }
            .debounce(150, TimeUnit.MILLISECONDS)
            .subscribe {
                println(it)
            }
    }

    /*
        distinct는 중목 발행된 항목은 무시한다.
     */
    @SuppressLint("CheckResult")
    fun testDistinct() {
        Observable.just(1, 2, 3, 2, 4, 1, 5)
            .distinct()
            .subscribe {
                println(it)
            }
    }


    /*
        sample은 일정 시간마다 아이템을 발행하되 직전에 소스 Observable에서 발행되었던 아이템을 발행한다.
        해당 시점과 다음 시점 사이에 아이템이 발행되지 않으면 그 시점에는 아이템 발행을 건너뛴다.
        예제에서는 100ms 시점 직전에 발행된 3이 발행되고
        200ms에서는 발행된 항목이 없어서 건너뛰고
        300ms에서는 4가 발행된다.
     */
    @SuppressLint("CheckResult")
    fun testSample() {
        Observable.create { emitter ->
            emitter.onNext(1)
            Thread.sleep(50)
            emitter.onNext(2)
            Thread.sleep(30)
            emitter.onNext(3)
            Thread.sleep(200)
            emitter.onNext(4)
        }
            .sample(100, TimeUnit.MILLISECONDS)
            .subscribe {
                println(it)
            }
    }

}