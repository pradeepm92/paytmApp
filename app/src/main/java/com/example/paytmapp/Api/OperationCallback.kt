package com.example.paytmapp.Api

import okhttp3.ResponseBody

interface OperationCallback<T> {
    fun onSuccess(data: T)
    fun onError(error: Int)
    fun onErrorMsg(error: ResponseBody?)

}