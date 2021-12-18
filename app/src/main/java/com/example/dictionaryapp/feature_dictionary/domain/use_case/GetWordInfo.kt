package com.example.dictionaryapp.feature_dictionary.domain.use_case

import com.example.dictionaryapp.core.util.Resource
import com.example.dictionaryapp.feature_dictionary.domain.model.WordInfo
import com.example.dictionaryapp.feature_dictionary.domain.repository.WordInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


/**這個use case是給UI端來看的，當UI有需求取得資料，就從這邊拿所以這個class要傳入repository來拿資料*/
class GetWordInfo(
    /**
     * 這裡放Interface是因為任何實現這個interface的Class都可以放進來取資料
     * Interface的特點就是只要是實現它一定會實現這個interface的所有方法
     * */
    val repository: WordInfoRepository
) {
    /**透過覆寫*/
    operator fun invoke(word:String):Flow<Resource<List<WordInfo>>>{
        /**如果使用者沒有輸入任何查詢字的話就回傳空的flow，就不會做任何的api call*/
        if (word.isBlank()){
            return flow {  }
        }
        /**如果有輸入字的話就去repository找字*/
        return repository.getWordInfo(word = word)
    }
}