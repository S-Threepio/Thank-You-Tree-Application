package com.example.thankyoutree.views.notes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.thankyoutree.model.Notes
import com.example.thankyoutree.model.liveDataReponses.NotesResponse
import com.example.thankyoutree.model.liveDataReponses.Status
import com.example.thankyoutree.retrofit.NotesApi
import com.example.thankyoutree.retrofit.RetrofitRepositoryImpl
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*
import retrofit2.Retrofit


class NotesViewModel : ViewModel() {
    val retrofitRepositoryImpl: Retrofit = RetrofitRepositoryImpl().get()
    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    var notesLiveData = MutableLiveData<NotesResponse>()

    val viewModelJob = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        uiScope.launch {
            notesLiveData.value = loading()
            try {
                fetchNotes()
            } catch (e: Throwable) {
                notesLiveData.value = error(e)
            }
        }
    }

    private suspend fun fetchNotes() {
        withContext(Dispatchers.IO) {
            retrofitRepositoryImpl.create(NotesApi::class.java)
                .getAllNotes().await().apply {
                    notesLiveData.postValue(success(Notes(this)))
                }
        }
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