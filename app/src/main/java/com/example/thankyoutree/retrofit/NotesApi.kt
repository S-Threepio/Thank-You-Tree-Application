package com.example.thankyoutree.retrofit

import com.example.thankyoutree.model.NamesOfEmployees
import com.example.thankyoutree.model.Note
import com.example.thankyoutree.model.Notes
import com.example.thankyoutree.model.Request
import io.reactivex.Single
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface NotesApi {
    @GET("dev/notes")
    fun getAllNotes(): Deferred<Array<Note>>

    @POST("dev/note")
    fun addNote(@Body note:Request):Deferred<Note>

    @GET(value = "dev")
    fun getListOfNames():Deferred<NamesOfEmployees>
}