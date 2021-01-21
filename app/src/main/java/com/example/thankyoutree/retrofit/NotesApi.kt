package com.example.thankyoutree.retrofit

import com.example.thankyoutree.model.NamesOfEmployees
import com.example.thankyoutree.model.Note
import com.example.thankyoutree.model.Request
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NotesApi {
    @GET("hsbc-dev/notes")
    fun getAllNotes(): Deferred<Array<Note>>

    @POST("hsbc-dev/note")
    fun addNote(@Body note:Request):Deferred<Note>

    @GET(value = "hsbc-dev")
    fun getListOfNames():Deferred<NamesOfEmployees>
}