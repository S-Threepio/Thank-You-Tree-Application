package com.example.thankyoutree.views.add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.thankyoutree.extensions.addTo
import com.example.thankyoutree.model.NamesOfEmployees
import com.example.thankyoutree.model.Request
import com.example.thankyoutree.model.liveDataReponses.AddNotesResponse
import com.example.thankyoutree.model.liveDataReponses.NamesResponse
import com.example.thankyoutree.model.liveDataReponses.Status
import com.example.thankyoutree.retrofit.NotesApi
import com.example.thankyoutree.retrofit.RetrofitRepositoryImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit


class AddNoteViewModel : ViewModel() {
    val retrofitRepositoryImpl: Retrofit = RetrofitRepositoryImpl().get()
    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    var addNoteLiveData = MutableLiveData<AddNotesResponse>()

    fun callApi(from: String, to: String, noteData: String) {
        var fromData = from
        var toData = to
        if (from.equals("")) {
            fromData = "-"
        }
        if (to.equals("")) {
            toData = "-"
        }

        val request = Request(toData, fromData, noteData)
        retrofitRepositoryImpl.create(NotesApi::class.java)
            .addNote(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                addNoteLiveData.value = loading()
            }
            .subscribe(
                {
                    addNoteLiveData.value = success()
                }, {
                    addNoteLiveData.value = error(it)
                }
            ).addTo(compositeDisposable)
    }

    fun loading(): AddNotesResponse? {
        return AddNotesResponse(
            Status.LOADING
        )
    }

    fun success(): AddNotesResponse? {
        return AddNotesResponse(
            Status.SUCCESS
        )
    }

    fun error(error: Throwable?): AddNotesResponse? {
        return AddNotesResponse(
            Status.ERROR,
            error = error
        )
    }
}