package com.example.thankyoutree.retrofit

import com.example.thankyoutree.model.NamesOfEmployees
import com.example.thankyoutree.model.Note
import com.example.thankyoutree.model.Notes
import com.example.thankyoutree.model.Request
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface NotesApi {
    @GET("dev/notes")
    fun getAllNotes(): Single<Array<Note>>

    @POST("dev/note")
    fun addNote(@Body note: Request):Single<Note>

    @GET(value = "dev")
    fun getListOfNames():Single<NamesOfEmployees>
}