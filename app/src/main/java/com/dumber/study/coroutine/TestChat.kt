package com.dumber.study.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class TestChat {

    private val channel = Channel<String>(1)

    fun start(): Flow<String> = flow {
        emit("Hello!")
        while(true) {
            try {
                val message = channel.receive()
                emit("$message Good!")
            } catch(e: Throwable) {
                return@flow
            }
        }
    }

    fun send(message: String) {
        CoroutineScope(Dispatchers.IO).launch {
            channel.send(message)
        }
    }

    fun stop() {
        channel.close()
    }


}