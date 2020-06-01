package com.example.thankyoutree.model.liveDataReponses

import com.example.thankyoutree.model.NamesOfEmployees

data class NamesResponse(
    val status: Status,
    val data: NamesOfEmployees? = null,
    val error: Throwable? = null
)

