package com.example.dacha.data.model

data class ResultProductModel(
    var rAmount: Double? = null,
    var rKey: String? = null,
    var pPrice: Double? = null,
    var rProduct: String? = null,
    var rWhose: List<SimplePersonModel>? = null
)