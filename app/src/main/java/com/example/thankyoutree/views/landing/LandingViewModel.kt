package com.example.thankyoutree.views.landing

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.thankyoutree.extensions.addTo
import com.example.thankyoutree.model.NamesOfEmployees
import com.example.thankyoutree.model.liveDataReponses.NamesResponse
import com.example.thankyoutree.model.liveDataReponses.Status
import com.example.thankyoutree.retrofit.NotesApi
import com.example.thankyoutree.retrofit.RetrofitRepositoryImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit


class LandingViewModel : ViewModel() {
    val retrofitRepositoryImpl: Retrofit = RetrofitRepositoryImpl().get()
    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    var landingLiveData =MutableLiveData<NamesResponse>()

    fun callApi() {
        retrofitRepositoryImpl.create(NotesApi::class.java)
            .getListOfNames()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                landingLiveData.value = loading()
            }
            .subscribe(
                {
                    landingLiveData.value = success(it)
                }, {
                    landingLiveData.value = error(it)
                }
            ).addTo(compositeDisposable)
    }

    fun loading(): NamesResponse? {
        return NamesResponse(
            Status.LOADING
        )
    }

    fun success(data: NamesOfEmployees): NamesResponse? {
        return NamesResponse(
            Status.SUCCESS,
            data
        )
    }

    fun error(error: Throwable?): NamesResponse? {
        return NamesResponse(
            Status.ERROR,
            error = error
        )
    }
}