package com.example.thankyoutree.model.liveDataReponses

import com.example.thankyoutree.model.Notes

data class AddNotesResponse(
    val status: Status,
    val error: Throwable? = null
)

