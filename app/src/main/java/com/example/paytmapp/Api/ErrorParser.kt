package com.example.paytmapp.Api

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody


class ErrorParser (private val errorResponse: ResponseBody) {
    private val TAG = this::class.simpleName

    // parsed response
    val parsedResponse: Map<String, Any> by lazy {
        val type = object : TypeToken<Map<String, Any>>() {}.type
        Gson().fromJson(errorResponse.charStream(), type)
    }

    // regular error message
    val message by lazy {
        parsedResponse.get("message").toString()
    }

    // validation errors
    val validationErrors by lazy {
        try {
            parsedResponse.get("errors") as Map<String, List<String>>
        } catch (e: Exception) {
            null
        }
    }

    // specific validation errors
    fun validationErrorsOf(key: String): List<String>? {
        return try {
            validationErrors?.get(key) as List<String>
        } catch (e: Exception) {
            null
        }
    }
}