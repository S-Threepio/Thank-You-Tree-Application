package com.example.thankyoutree.landing

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.thankyoutree.AddNoteFragment
import com.example.thankyoutree.extensions.addTo
import com.example.thankyoutree.extensions.replace
import com.example.thankyoutree.model.NamesOfEmployees
import com.example.thankyoutree.retrofit.NotesApi
import com.example.thankyoutree.retrofit.RetrofitRepositoryImpl
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit

class LandingViewModel : ViewModel() {
    val retrofitRepositoryImpl: Retrofit = RetrofitRepositoryImpl().get()
    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    val liveDataSuccess: MutableLiveData<NamesOfEmployees> = MutableLiveData()
    val liveDataError: MutableLiveData<Throwable> = MutableLiveData()

    fun callApi() {
        retrofitRepositoryImpl.create(NotesApi::class.java)
            .getListOfNames()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    liveDataSuccess.value = it
                }, {
                    liveDataError.value = it
                }
            )
    }
}