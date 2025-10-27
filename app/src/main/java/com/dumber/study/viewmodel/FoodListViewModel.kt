package com.dumber.study.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dumber.study.viewmodel.model.Food
import com.dumber.study.viewmodel.model.FoodModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class FoodListState(
    var isLoading: Boolean = false,
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
                    isLoading = true
                )
            }

            val response = FoodModel.foodApiService.getFoodList()
            if (response.isSuccessful) {

                response.body()?.also { data ->
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false
                            , items = data.foods
                            , nextCursor = data.paging.nextCursor
                        )
                    }
                }
            }
        }
    }

    fun loadNextList() {
        if (_uiState.value.nextCursor == null || _uiState.value.isLoading)
            return

        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    isLoading = true
                )
            }

            val response = FoodModel.foodApiService.getFoodList(cursor = _uiState.value.nextCursor)
            if (response.isSuccessful) {

                response.body()?.also { data ->
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false
                            , items = state.items + data.foods
                            , nextCursor = data.paging.nextCursor
                        )
                    }
                }
            }
        }
    }
    
}