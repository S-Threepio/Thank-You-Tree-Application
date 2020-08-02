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
import kotlinx.coroutines.*
import retrofit2.Retrofit


class LandingViewModel : ViewModel() {
    val retrofitRepositoryImpl: Retrofit = RetrofitRepositoryImpl().get()
    var landingLiveData = MutableLiveData<NamesResponse>()
    val viewModelJob = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    fun callApi() {
        uiScope.launch {
            landingLiveData.value = loading()
            try {
                fetchNames()
            } catch (e: Throwable) {
                landingLiveData.value = error(e)
            }
        }
    }

    private suspend fun fetchNames() {
        withContext(Dispatchers.IO) {
            retrofitRepositoryImpl.create(NotesApi::class.java)
                .getListOfNames().await().apply {
                    landingLiveData.postValue(success(this))
                }
        }
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