package com.example.dictionaryapp.feature_dictionary.data.util

import java.lang.reflect.Type


interface JsonParser {

    fun <T> fromJson(json: String, type: Type): T
    /**因為進來的obj有可能是 null 轉出來的型態就有可能是沒東西所以用 nullable*/
    fun <T> toJson(obj: T, type: Type): String?
}