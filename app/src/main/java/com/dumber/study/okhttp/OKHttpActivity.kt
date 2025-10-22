package com.dumber.study.okhttp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dumber.study.databinding.ActivityOkhttpBinding
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException

class OKHttpActivity: AppCompatActivity() {

    lateinit var binding: ActivityOkhttpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOkhttpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGetSync.setOnClickListener {
            Thread {
                requestGETSync()
            }.start()
        }

        binding.btnGetAsync.setOnClickListener {
            requestGETAsync()
        }


    }

    data class Item(val userId: Int, val id: Int, val title: String, val completed: Boolean) {
        override fun toString(): String {
            return "[$id/$userId] $title ($completed)"
        }
    }

    fun requestGETSync() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://jsonplaceholder.typicode.com/todos")
            .build()

        client.newCall(request)
            .execute().use { response ->
                if (response.isSuccessful) {
                    val array = JsonParser.parseString(response.body.string()) as JsonArray
                    val gson = Gson()
                    val items = array.map { gson.fromJson(it, Item::class.java) }
                    items.forEach { println(it) }
                }
            }
    }

    fun requestGETAsync() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://jsonplaceholder.typicode.com/todos")
            .build()

        client.newCall(request)
            .enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val array = JsonParser.parseString(response.body.string()) as JsonArray
                        val gson = Gson()
                        val items = array.map { gson.fromJson(it, Item::class.java) }
                        items.forEach { println(it) }
                    }
                }
            })

    }

}