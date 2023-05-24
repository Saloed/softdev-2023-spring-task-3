package com.example.dacha.data.model

data class EventModel(
    var eInfo: EventInfo? = null,
    var ePeople: List<SimplePersonModel>? = null,
    var ePlanProducts: Map<String, PlanProductModel>? = null,
    var ePurchases: Map<String, PurchaseModel>? = null
)
