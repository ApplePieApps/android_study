package com.dumber.study.livedata

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dumber.study.R
import com.dumber.study.databinding.ActivityLiveDataBinding

data class MyData(var name: LiveData<String>, var gender: String, var age: Int)

class LiveDataActivity: AppCompatActivity() {
    lateinit var binding: ActivityLiveDataBinding

    val myName = MutableLiveData("정건국")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_live_data)
        binding.activity = this
        binding.lifecycleOwner = this

        binding.etName.setText(myName.value)
        binding.etName.addTextChangedListener { editable ->
            myName.value = editable.toString()
        }
    }

}