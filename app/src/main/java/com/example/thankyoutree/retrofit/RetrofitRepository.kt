package com.example.thankyoutree.retrofit

interface RetrofitRepository<T> {
    fun get(): T
}