package com.example.paytmapp

import android.content.Context
import android.content.SharedPreferences

object Utlis {
    fun getBaseUrl(context: Context): String? {
        return "https://zeoner.com/"
    }



    fun getAuthToken(context: Context): String {

        return "Bearer " + "978|npiwUJPVf3D4F90dECPr5U7WVniYfYFIEhbGFxbg"
    }
}