package com.example.dictionaryapp.feature_dictionary.data.repository

import com.example.dictionaryapp.core.util.Resource
import com.example.dictionaryapp.feature_dictionary.data.local.WordInfoDao
import com.example.dictionaryapp.feature_dictionary.data.remote.DictionaryApi
import com.example.dictionaryapp.feature_dictionary.domain.model.WordInfo
import com.example.dictionaryapp.feature_dictionary.domain.repository.WordInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class WordInfoRepositoryImpl(
    val api: DictionaryApi,
    val dao: WordInfoDao
) : WordInfoRepository {
    /**
     * 在這邊我不會直接取得api data直接反映在UI，而是透過api取得資料後插入database然後UI再去讀database的資料更新
     * 因為domain的dataclass 是給view使用的 所以當透過dao取得資料後再把資料mapping到domain
     *
     * 動作: API讀取資料 -> 更新Room{ 刪除舊資料 -> 插入新資料 } -> 讀取Room剛更新的資料 -> Flow取出來給UI更新
     * */
    override fun getWordInfo(word: String): Flow<Resource<List<WordInfo>>> = flow {
        /**開始讀取的狀態*/
        emit(Resource.Loading())

        /**讀取舊資料*/
        val wordInfos = dao.getWordInfo(word = word).map { it.toWordInfo() }

        /**讀取後*/
        emit(Resource.Loading(data = wordInfos))

        try {
            /**Database更新: 當api request資料後將原本舊的資料刪除再插入新的資料達到更新的動作*/
            val remoteWordInfos = api.getWordInfo(word = word)
            dao.deleteWordInfo(remoteWordInfos.map { it.word })
            dao.insertWordInfos(remoteWordInfos.map { it.toWordInfoEntity() })
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = "Something went wrong.",
                    data = wordInfos
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = "Couldn't reach server, check your internet connection.",
                    data = wordInfos
                )
            )
        }
        /**上面動作完成更新之後再從database拿出資料放進Flow給UI更新*/
        val newWordInfos = dao.getWordInfo(word).map { it.toWordInfo() }
        emit(Resource.Success(newWordInfos))
    }
}