package com.example.dacha.data.repository

import android.net.Uri
import com.example.dacha.data.model.*
import com.example.dacha.utils.UiState


interface ProductRepository {
    fun addEvent(event: EventModel, result: (UiState<EventModel>) -> Unit)
    fun updateEvent(event: EventModel, result: (UiState<EventModel>) -> Unit)
    fun deleteEvent(event: EventModel, result: (UiState<EventModel>) -> Unit)
    fun chooseEvent(event: EventModel, result: (UiState<EventModel>) -> Unit)
    suspend fun getEvents(): UiState<List<EventModel>>
    fun getChosenEvent(result: (UiState<String?>) -> Unit)
    fun addPlanProduct(event: String, planProduct: PlanProductModel, result: (UiState<PlanProductModel>) -> Unit)
    fun updatePlanProduct(event: String, planProduct: PlanProductModel, result: (UiState<PlanProductModel>) -> Unit)
    fun deletePlanProduct(event: String, planProduct: PlanProductModel, result: (UiState<PlanProductModel>) -> Unit)
    suspend fun getPlanProducts(event: String): UiState<List<PlanProductModel>>
    fun addPurchase(event: String, purchase: PurchaseModel, result: (UiState<PurchaseModel>) -> Unit)
    fun updatePurchase(event: String, purchase: PurchaseModel, result: (UiState<PurchaseModel>) -> Unit)
    fun deletePurchase(event: String, purchase: PurchaseModel, result: (UiState<PurchaseModel>) -> Unit)
    suspend fun getPurchase(event: String): UiState<List<PurchaseModel>>
    suspend fun uploadCheck(fileUri: Uri, onResult: (UiState<Pair<Uri, String>>) -> Unit)
}