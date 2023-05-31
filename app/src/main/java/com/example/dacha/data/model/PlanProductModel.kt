package com.example.dacha.data.model

data class PlanProductModel(
    var pAmount: Double? = null,
    var pKey: String? = null,
    var pProduct: String? = null,
    var pWhose: List<SimplePersonModel>? = null
)
