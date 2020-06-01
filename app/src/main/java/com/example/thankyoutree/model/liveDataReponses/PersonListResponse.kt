package com.example.thankyoutree.model.liveDataReponses

import com.example.thankyoutree.model.Notes
import com.example.thankyoutree.model.Person

data class PersonListResponse(
    val status: Status,
    val data: Array<Person>? = null,
    val error: Throwable? = null
)

