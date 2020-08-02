package com.example.thankyoutree.views.add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.thankyoutree.model.Request
import com.example.thankyoutree.model.liveDataReponses.AddNotesResponse
import com.example.thankyoutree.model.liveDataReponses.Status
import com.example.thankyoutree.retrofit.NotesApi
import com.example.thankyoutree.retrofit.RetrofitRepositoryImpl
import kotlinx.coroutines.*
import retrofit2.Retrofit


class AddNoteViewModel : ViewModel() {
    val retrofitRepositoryImpl: Retrofit = RetrofitRepositoryImpl().get()
    var addNoteLiveData = MutableLiveData<AddNotesResponse>()
    val viewModelJob = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

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

        uiScope.launch {
            addNoteLiveData.value = loading()

            try {
                addNote(request)
            } catch (e: Throwable) {
                addNoteLiveData.value = error(e)
            }
        }
    }

    private suspend fun addNote(request: Request) {
        withContext(Dispatchers.IO) {
            retrofitRepositoryImpl.create(NotesApi::class.java).addNote(request).await().apply {
                addNoteLiveData.postValue(success())
            }
        }
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