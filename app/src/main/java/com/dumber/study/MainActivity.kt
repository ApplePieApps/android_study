package com.dumber.study

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dumber.study.coroutine.CoroutineActivity
import com.dumber.study.dataBinding.DataBindingActivity
import com.dumber.study.databinding.ActivityMainBinding
import com.dumber.study.livedata.LiveDataActivity
import com.dumber.study.okhttp.OKHttpActivity
import com.dumber.study.rxkotlin.RxKotlinTest
import com.dumber.study.viewmodel.ViewModelActivity

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnRxKotlin.setOnClickListener { RxKotlinTest().test() }
        binding.btnCoroutine.setOnClickListener { startActivity(Intent(this, CoroutineActivity::class.java)) }
        binding.btnExecutor.setOnClickListener { ExecutorTest().test() }
        binding.btnViewModel.setOnClickListener { startActivity(Intent(this, ViewModelActivity::class.java)) }
        binding.btnLiveData.setOnClickListener { startActivity(Intent(this, LiveDataActivity::class.java)) }
        binding.btnDataBinding.setOnClickListener { startActivity(Intent(this, DataBindingActivity::class.java)) }
        binding.btnRoom.setOnClickListener { RoomTest().test() }
        binding.btnRetrofit.setOnClickListener { RetrofitTest().test() }
        binding.btnOkHttp.setOnClickListener { startActivity(Intent(this, OKHttpActivity::class.java)) }

    }

    companion object {
        const val TAG = "AndroidStudy"
    }

}