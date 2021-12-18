package com.example.dictionaryapp.feature_dictionary.domain.repository

import com.example.dictionaryapp.core.util.Resource
import com.example.dictionaryapp.feature_dictionary.domain.model.WordInfo
import kotlinx.coroutines.flow.Flow


interface WordInfoRepository {
    /**要使用Kotlin的Flow 透過Resource包起來
     * 因為Resource可以知道當前的資料讀取狀態
     * 如果List<WordInfo>裡面有資料
     * 就會是Success狀態
     * */
    fun getWordInfo(word: String): Flow<Resource<List<WordInfo>>>
}