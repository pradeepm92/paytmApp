package com.example.paytmapp.Api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class APIClient(private val baseURL: String) {



    val instance: Api
        get() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val headerInterceptor = Interceptor { chain ->
                val request = chain.request().newBuilder().addHeader("Accept", "application/json").build()
                chain.proceed(request)
            }

//            val instance_new = UnsafeOkHttpClient.getUnsafeOkHttpClient()
            val instance_new = OkHttpClient.Builder().addInterceptor(interceptor)
                .addInterceptor(interceptor)
                .addInterceptor(headerInterceptor)
                .callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .build()

            return Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(instance_new)
                .build()

                .create(Api::class.java)
        }

//9344427887


}