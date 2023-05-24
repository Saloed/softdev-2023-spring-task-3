package com.example.dacha.data.repository

import android.net.Uri
import com.example.dacha.data.model.EventModel
import com.example.dacha.data.model.PersonModel
import com.example.dacha.data.model.PlanProductModel
import com.example.dacha.data.model.PurchaseModel
import com.example.dacha.utils.UiState


interface ProductRepository {
    fun addEvent(event: EventModel, result: (UiState<Pair<EventModel, String>>) -> Unit)
    fun updateEvent(event: EventModel, result: (UiState<Pair<EventModel, String>>) -> Unit)
    fun deleteEvent(event: EventModel, result: (UiState<Pair<EventModel, String>>) -> Unit)
    fun chooseEvent(event: EventModel, result: (UiState<Pair<EventModel, String>>) -> Unit)
    fun getEvents(result: (UiState<List<EventModel>>) -> Unit)
    fun getChosenEvent(result: (UiState<String?>) -> Unit)
    fun getPeople(result: (UiState<List<PersonModel>>) -> Unit)
    fun addPlanProduct(event: String, planProduct: PlanProductModel, result: (UiState<Pair<PlanProductModel, String>>) -> Unit)
    fun updatePlanProduct(event: String, planProduct: PlanProductModel, result: (UiState<Pair<PlanProductModel, String>>) -> Unit)
    fun deletePlanProduct(event: String, planProduct: PlanProductModel, result: (UiState<Pair<PlanProductModel, String>>) -> Unit)
    fun getPlanProducts(event: String, result: (UiState<List<PlanProductModel>>) -> Unit)
    fun addPurchase(event: String, purchase: PurchaseModel, result: (UiState<Pair<PurchaseModel, String>>) -> Unit)
    fun updatePurchase(event: String, purchase: PurchaseModel, result: (UiState<Pair<PurchaseModel, String>>) -> Unit)
    fun deletePurchase(event: String, purchase: PurchaseModel, result: (UiState<Pair<PurchaseModel, String>>) -> Unit)
    fun getPurchase(event: String, result: (UiState<List<PurchaseModel>>) -> Unit)
    suspend fun uploadCheck(fileUri: Uri, onResult: (UiState<Pair<Uri, String>>) -> Unit)
}