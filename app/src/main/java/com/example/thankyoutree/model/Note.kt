package com.example.thankyoutree.model

data class Note(
    val createdAt :String,
    val id: String,
    val to: String,
    val from: String,
    val noteData: String,
    val userId:Int
)

data class Notes(
    val notes:Array<Note>
)
