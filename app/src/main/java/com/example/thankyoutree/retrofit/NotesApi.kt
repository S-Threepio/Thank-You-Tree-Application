package com.example.thankyoutree.retrofit

import com.example.thankyoutree.model.NamesOfEmployees
import com.example.thankyoutree.model.Note
import com.example.thankyoutree.model.Notes
import com.example.thankyoutree.model.Request
import io.reactivex.Single
import retrofit2.http.*

interface NotesApi {
    @GET("dev/notes")
    fun getAllNotes(): Single<Array<Note>>

    @FormUrlEncoded
    @POST("dev/note")
    fun addNote(@Field("note") note:Request):Single<Note>

    @GET(value = "dev")
    fun getListOfNames():Single<NamesOfEmployees>
}