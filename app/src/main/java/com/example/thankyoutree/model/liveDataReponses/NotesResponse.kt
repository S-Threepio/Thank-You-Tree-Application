package com.example.thankyoutree.model.liveDataReponses

import com.example.thankyoutree.model.Notes

data class NotesResponse(
    val status: Status,
    val data: Notes? = null,
    val error: Throwable? = null
)

