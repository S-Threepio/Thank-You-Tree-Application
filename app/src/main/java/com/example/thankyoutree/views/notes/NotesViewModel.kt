package com.example.thankyoutree.views.notes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.thankyoutree.extensions.addTo
import com.example.thankyoutree.model.NamesOfEmployees
import com.example.thankyoutree.model.Notes
import com.example.thankyoutree.model.liveDataReponses.NamesResponse
import com.example.thankyoutree.model.liveDataReponses.NotesResponse
import com.example.thankyoutree.model.liveDataReponses.Status
import com.example.thankyoutree.retrofit.NotesApi
import com.example.thankyoutree.retrofit.RetrofitRepositoryImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit


class NotesViewModel : ViewModel() {
    val retrofitRepositoryImpl: Retrofit = RetrofitRepositoryImpl().get()
    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    var notesLiveData =MutableLiveData<NotesResponse>()

    fun callApi() {
        retrofitRepositoryImpl.create(NotesApi::class.java)
            .getAllNotes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                notesLiveData.value = loading()
            }
            .subscribe(
                {
                    notesLiveData.value = success(Notes(it))
                }, {
                    notesLiveData.value = error(it)
                }
            ).addTo(compositeDisposable)
    }

    fun loading(): NotesResponse? {
        return NotesResponse(
            Status.LOADING
        )
    }

    fun success(data: Notes): NotesResponse? {
        return NotesResponse(
            Status.SUCCESS,
            data
        )
    }

    fun error(error: Throwable?): NotesResponse? {
        return NotesResponse(
            Status.ERROR,
            error = error
        )
    }
}