package com.example.thankyoutree.retrofit

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitProvider : Provider<Retrofit> {
    val BASE_URL = " https://z0y9tg6nl3.execute-api.ap-south-1.amazonaws.com/"
    val gson = GsonConverterFactory.create()

    private val logging: HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    override fun get(): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gson)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }
}