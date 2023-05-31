package com.example.dacha.data.model

data class TransactionModel(
    var from: PersonModel? = null,
    var to: PersonModel? = null,
    var howMuch: Int? = null,
    var key: String? = null
)
