package com.example.dictionaryapp.feature_dictionary.data.local

import androidx.room.*
import com.example.dictionaryapp.feature_dictionary.data.local.entity.WordInfoEntity
import com.example.dictionaryapp.feature_dictionary.domain.model.WordInfo

@Dao
interface WordInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWordInfos(infos: List<WordInfoEntity>)

    @Query("delete from wordinfoentity where word in (:words)")
    suspend fun deleteWordInfo(words: List<String>)

    @Query("select * from wordinfoentity where word like '%' || (:word) || '%'")
    fun getWordInfo(word:String) : List<WordInfoEntity>
}