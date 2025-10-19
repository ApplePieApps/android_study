package com.dumber.study.coroutine

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull


class CoroutineTest {

    fun test() {
//        testLaunch()
//        testAsync()
//        testWithContext()
//        testTimeout()
        testExceptionHandler()
    }

    fun testLaunch() {
        // CoroutineScope로 launch를 실행하면 Job이 생성됨
        CoroutineScope(Dispatchers.Main + CoroutineName("testLaunch")).launch {
            val job = CoroutineScope(Dispatchers.IO + CoroutineName("LaunchTest")).launch {
                println("Start Hello")
                repeat(10) {
                    delay(500)
                    println("Hello #${it}")
                }
                println("Stop Hello")
            }
            job.join()
        }
    }

    fun testAsync() {
        // CoroutineScope로 async가 실행하면 Deferred가 생성됨
        // Deferred는 await()로 결과를 받아옴
        CoroutineScope(Dispatchers.Main + CoroutineName("testAsync")).launch {
            val deferred1 = CoroutineScope(Dispatchers.IO + CoroutineName("AsyncTest1")).async {
                println("Start Async1")
                delay(2000)
                println("Finish Async1")
                "Good1"
            }
            val deferred2 = CoroutineScope(Dispatchers.IO + CoroutineName("AsyncTest2")).async {
                println("Start Async2")
                delay(1050)
                println("Finish Async2")
                "Good2"
            }
            val results = listOf(deferred1, deferred2).awaitAll()
            println("Results : $results")
        }
    }

    fun testWithContext() {
        CoroutineScope(Dispatchers.Main + CoroutineName("testWithContext")).launch {
            val result = withContext(Dispatchers.IO + CoroutineName("withContext")) {
                println("Start withContext ${currentCoroutineContext()[CoroutineName]?.name}")
                delay(1000)
                println("Finish withContext")
                "Wow!!"
            }
            println("Result: $result")
        }
    }

    fun testTimeout() {
        CoroutineScope(Dispatchers.Main + CoroutineName("TimeoutTest")).launch {
            try {
                val result = withTimeout(2000L) {
                    println("Start withTimeout")
                    delay(2100)
                    println("Finish withTimeout")
                    "Wow!!"
                }
                println("Result: $result")
            } catch(e: TimeoutCancellationException) {
                println("Timeout!! : $e")
            }

            val result = withTimeoutOrNull(2000L) {
                println("Start withTimeoutOrNull")
                delay(2100)
                println("Finish withTimeoutOrNull")
                "Wow!!"
            }
            println("Result: $result")
        }
    }

    fun testExceptionHandler() {
        // Timeout과 같은 CancellationException은 CoroutineExceptionHandler에서 처리되지 않음
        CoroutineScope(Dispatchers.Main
            + CoroutineName("testExceptionHandler")
            + CoroutineExceptionHandler { context, e ->
                println("Exception!! : $e")
            }
        ).launch {
            try {
                withTimeout(100) {
                    println("start timeout")
                    delay(1000)
                    println("finish timeout")
                }
            } catch (e: Throwable) {
                throw Exception("Timeout!!!")
            }
        }
    }

}

suspend fun CoroutineScope.println(message: String) {
    val context = currentCoroutineContext()
    val coroutineName = context[CoroutineName]?.name ?: "NoName"
    kotlin.io.println("[$coroutineName/${Thread.currentThread().name}] $message")
}