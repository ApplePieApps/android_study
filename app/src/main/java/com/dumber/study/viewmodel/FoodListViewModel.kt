package com.dumber.study.viewmodel

import android.R
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dumber.study.viewmodel.model.Food
import com.dumber.study.viewmodel.model.FoodModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class FoodListEvent {
    List,
    MoreList,
    Delete,
    Add,
    Loading,
    MoreLoading,
}

data class FoodListState(
    var event: FoodListEvent = FoodListEvent.List,
    var items: List<Food> = emptyList(),
    var nextCursor: String? = null,
)
class FoodListViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(FoodListState())
    val uiState = _uiState.asStateFlow()

    init {
        loadList()
    }

    fun loadList() {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    event = FoodListEvent.Loading
                )
            }

            val response = FoodModel.foodApiService.getFoodList()
            if (response.isSuccessful) {

                response.body()?.also { data ->
                    _uiState.update { state ->
                        state.copy(
                            event = FoodListEvent.List
                            , items = data.foods
                            , nextCursor = data.paging.nextCursor
                        )
                    }
                }
            }
        }
    }

    fun loadNextList() {
        if (_uiState.value.nextCursor == null
            || _uiState.value.event == FoodListEvent.Loading
            ||  _uiState.value.event == FoodListEvent.MoreLoading)
            return

        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    event = FoodListEvent.MoreLoading
                )
            }

            val response = FoodModel.foodApiService.getFoodList(cursor = _uiState.value.nextCursor)
            if (response.isSuccessful) {

                response.body()?.also { data ->
                    _uiState.update { state ->
                        state.copy(
                            event = FoodListEvent.MoreList
                            , items = data.foods
                            , nextCursor = data.paging.nextCursor
                        )
                    }
                }
            }
        }
    }

}