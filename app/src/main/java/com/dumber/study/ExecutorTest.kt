package com.dumber.study

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class ExecutorTest {

    fun test() {
        CoroutineScope(Dispatchers.IO).launch {
            testFixedExecutor()
            testSingleThread()
        }
    }

    fun testFixedExecutor() {
        println("-- testFixedExecutor start --")

        val executor = Executors.newFixedThreadPool(4)
        repeat(10) {
            executor.execute {
                println("Start #$it in ${Thread.currentThread().name}")
                Thread.sleep(1000L + Random.nextInt(3000))
                println("Finish #$it in ${Thread.currentThread().name}")
            }
        }
        executor.shutdown()
        if (!executor.awaitTermination(10, TimeUnit.SECONDS))
            executor.shutdownNow()

        println("-- testFixedExecutor finished --\n\n")
    }

    fun testSingleThread() {
        println("-- testSingleThread start --")

        val executor = Executors.newSingleThreadExecutor()
        repeat(10) {
            executor.execute {
                println("Start #$it in ${Thread.currentThread().name}")
                try {
                    Thread.sleep(1000L + Random.nextInt(3000))
                } catch (_: InterruptedException) {
                    println("Inturrupted #$it in ${Thread.currentThread().name}")
                    return@execute
                }
                println("Finish #$it in ${Thread.currentThread().name}")
            }
        }
        executor.shutdown()
        if (!executor.awaitTermination(10, TimeUnit.SECONDS))
            executor.shutdownNow()

        println("-- testSingleThread finished --\n\n")
    }

}