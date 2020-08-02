package com.example.thankyoutree.views.dashboard.helper

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.thankyoutree.model.Note
import com.example.thankyoutree.model.Notes
import com.example.thankyoutree.model.Person
import com.example.thankyoutree.model.liveDataReponses.PersonListResponse
import com.example.thankyoutree.model.liveDataReponses.Status
import com.example.thankyoutree.retrofit.NotesApi
import com.example.thankyoutree.retrofit.RetrofitRepositoryImpl
import kotlinx.coroutines.*
import retrofit2.Retrofit


class HelperViewModel : ViewModel() {
    val retrofitRepositoryImpl: Retrofit = RetrofitRepositoryImpl().get()
    var _helperLiveData = MutableLiveData<PersonListResponse>()
    val helperLiveData: LiveData<PersonListResponse>
        get() = _helperLiveData
    val viewModelJob = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        uiScope.launch {
            _helperLiveData.value = loading()
            try {
                fetchNotes()
            } catch (e: Throwable) {
                _helperLiveData.value = error(e)
            }
        }
    }

    private suspend fun fetchNotes() {
        withContext(Dispatchers.IO) {
            retrofitRepositoryImpl.create(NotesApi::class.java)
                .getAllNotes().await().apply {
                    _helperLiveData.postValue(success(Notes(this)))
                }
        }
    }

    fun loading(): PersonListResponse? {
        return PersonListResponse(
            Status.LOADING
        )
    }

    fun success(data: Notes): PersonListResponse? {
        return PersonListResponse(
            Status.SUCCESS,
            makePersonList(data.notes)
        )
    }

    fun error(error: Throwable?): PersonListResponse? {
        return PersonListResponse(
            Status.ERROR,
            error = error
        )
    }

    private fun makePersonList(notes: Array<Note>): Array<Person> {

        val countingData: ArrayList<Person> = ArrayList()
        var flag = false
        for (note in notes) {
            if (note.to == "-")
                continue
            if (countingData.isEmpty())
                countingData.add(Person(note.to, 0))
            flag = false
            for (person in countingData) {
                if (note.to == person.name) {
                    person.count = person.count + 1
                    flag = true
                }
            }
            if (!flag)
                countingData.add(Person(note.to, 1))
        }
        val arrayOfData: Array<Person> = countingData.toTypedArray<Person>().also { it.sort() }
        return arrayOfData
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}