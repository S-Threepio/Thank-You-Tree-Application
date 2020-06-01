package com.example.thankyoutree.views.dashboard.helper

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.thankyoutree.extensions.addTo
import com.example.thankyoutree.model.Note
import com.example.thankyoutree.model.Notes
import com.example.thankyoutree.model.Person
import com.example.thankyoutree.model.liveDataReponses.PersonListResponse
import com.example.thankyoutree.model.liveDataReponses.Status
import com.example.thankyoutree.retrofit.NotesApi
import com.example.thankyoutree.retrofit.RetrofitRepositoryImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit


class HelperViewModel : ViewModel() {
    val retrofitRepositoryImpl: Retrofit = RetrofitRepositoryImpl().get()
    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    var helperLiveData = MutableLiveData<PersonListResponse>()

    fun callApi() {
        retrofitRepositoryImpl.create(NotesApi::class.java)
            .getAllNotes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                helperLiveData.value = loading()
            }
            .subscribe(
                {
                    helperLiveData.value = success(Notes(it))
                }, {
                    helperLiveData.value = error(it)
                }
            ).addTo(compositeDisposable)
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
        val arrayOfData = arrayOfNulls<Person>(countingData.size)
        countingData.toArray(arrayOfData).sort()
        return countingData.toArray(arrayOfData)
    }

}