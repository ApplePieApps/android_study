package com.dumber.study.viewmodel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.dumber.study.databinding.ActivityViewmodelBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ViewModelActivity: AppCompatActivity() {

    lateinit var binding: ActivityViewmodelBinding

    var foodViewModel: FoodListViewModel? = null
    val foodAdapter = FoodListAdapter() {
        foodViewModel?.loadNextList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityViewmodelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.listView.adapter = foodAdapter

        foodViewModel = ViewModelProvider.create(this).get(FoodListViewModel::class.java)
        lifecycleScope.launch {
            foodViewModel!!.uiState.collect { state ->
                foodAdapter.update(state)
            }
        }

    }


}