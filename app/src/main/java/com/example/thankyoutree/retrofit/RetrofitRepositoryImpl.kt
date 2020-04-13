package com.example.thankyoutree.retrofit

import retrofit2.Retrofit

class RetrofitRepositoryImpl:RetrofitRepository<Retrofit> {

    val retrofitProvider:RetrofitProvider= RetrofitProvider()
    override fun get(): Retrofit {
        return retrofitProvider.get()
    }
}