package com.example.dacha.data.model

data class PurchaseModel(
    var purchaseInfo: PurchaseInfo? = null,
    var resultProducts: Map<String, ResultProductModel>? = null
)
