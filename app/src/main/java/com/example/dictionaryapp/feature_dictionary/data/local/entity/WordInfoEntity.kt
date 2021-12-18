package com.example.dictionaryapp.feature_dictionary.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.dictionaryapp.feature_dictionary.domain.model.Meaning
import com.example.dictionaryapp.feature_dictionary.domain.model.WordInfo


/**
 * 這裡有一個問題：因為我們要寫入Room，可是仔細看meanings他會寫入一個Meaning型態的List
 * 所以在JsonParser我們創在一個可以將Meaning 轉成 Room讀的懂的型態String
 * */
@Entity
data class WordInfoEntity(
    val word:String,
    val phonetic:String,
    val origin:String,
    val meanings:List<Meaning>,
    @PrimaryKey(autoGenerate = true) val id:Int? = null
){
    fun toWordInfo():WordInfo{
        return WordInfo(
            meanings = meanings,
            origin = origin,
            word = word,
            phonetic = phonetic
        )
    }
}
