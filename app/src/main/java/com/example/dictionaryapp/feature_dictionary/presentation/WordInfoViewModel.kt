package com.example.dictionaryapp.feature_dictionary.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dictionaryapp.core.util.Resource
import com.example.dictionaryapp.feature_dictionary.domain.use_case.GetWordInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordInfoViewModel @Inject constructor(
    private val getWordInfo: GetWordInfo
) : ViewModel() {

    /**當使用者輸入：
     * searchQuery (State) -> 負責存放輸入字串
     * searchJob (Coroutines Job) -> 負責執行搜索邏輯
     * state (State) -> 將被Resource包起來的資料，檢查是成功還是失敗
     * eventFlow (Flow) -> 通知SnackBar
     * */
    private val _searchQuery = mutableStateOf<String>("")
    val searchQuery: State<String> = _searchQuery

    private val _state = mutableStateOf<WordInfoState>(WordInfoState())
    val state: State<WordInfoState> = _state

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var searchJob: Job? = null


    fun onSearch(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch (Dispatchers.IO){
            delay(500L)
            getWordInfo(query)
                .onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                wordInfoItems = result.data ?: emptyList(),
                                isLoading = false
                            )
                        }
                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                wordInfoItems = result.data ?: emptyList(),
                                isLoading = false
                            )
                            _eventFlow.emit(
                                UIEvent.ShowSnackBar(
                                    result.message ?: "Unknown error"
                                )
                            )
                        }
                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                wordInfoItems = result.data ?: emptyList(),
                                isLoading = true
                            )
                        }
                    }
                }.launchIn(this)
        }
    }

    /**網路連接狀態*/
    sealed class UIEvent {
        /**處理SnackBar的狀態*/
        data class ShowSnackBar(val message: String) : UIEvent()
    }
}













