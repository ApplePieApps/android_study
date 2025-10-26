package com.dumber.study.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dumber.study.viewmodel.model.FoodItem
import com.dumber.study.viewmodel.model.FoodModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FoodListViewModel: ViewModel() {

    private val _foodList = MutableLiveData<List<FoodItem>>()

    val foodList: LiveData<List<FoodItem>>
        get() = _foodList

    init {
        loadList()
    }

    fun loadList() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = FoodModel.foodApiService.getFoodList()
            if (response.isSuccessful) {
                response.body()?.also {
                    withContext(Dispatchers.Main) {
                        _foodList.value = it.foods
                    }
                }
            }
        }
    }

}