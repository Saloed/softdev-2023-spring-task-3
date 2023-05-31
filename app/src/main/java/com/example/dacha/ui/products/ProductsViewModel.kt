package com.example.dacha.ui.products

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dacha.data.model.EventModel
import com.example.dacha.data.model.PlanProductModel
import com.example.dacha.data.model.PurchaseModel
import com.example.dacha.data.repository.ProductRepository
import com.example.dacha.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(val repository: ProductRepository) : ViewModel() {
    private val _addEvent = MutableLiveData<UiState<EventModel>>()
    val addEvent: LiveData<UiState<EventModel>> = _addEvent

    fun addEvent(event: EventModel) {
        _addEvent.value = UiState.Loading
        repository.addEvent(event) {_addEvent.value = it}
    }

    private val _updateEvent = MutableLiveData<UiState<EventModel>>()
    val updateEvent: LiveData<UiState<EventModel>> = _updateEvent

    fun updateEvent(event: EventModel) {
        _updateEvent.value = UiState.Loading
        repository.updateEvent(event) {_updateEvent.value = it}
    }

    private val _deleteEvent = MutableLiveData<UiState<EventModel>>()
    val deleteEvent: LiveData<UiState<EventModel>> = _deleteEvent

    fun deleteEvent(event: EventModel) {
        _deleteEvent.value = UiState.Loading
        repository.deleteEvent(event) {_deleteEvent.value = it}
    }

    private val _events = MutableLiveData<UiState<List<EventModel>>>()
    val events: LiveData<UiState<List<EventModel>>> = _events

    fun getEvents() = viewModelScope.launch {
        _events.value = UiState.Loading
        val result = repository.getEvents()
        _events.value = result
    }

    private val _chooseEvent = MutableLiveData<UiState<EventModel>>()
    val chooseEvent: LiveData<UiState<EventModel>> = _chooseEvent

    fun chooseEvent(event: EventModel) {
        _chooseEvent.value = UiState.Loading
        repository.chooseEvent(event) {_chooseEvent.value = it}
    }

    private val _chosenEvent = MutableLiveData<UiState<String?>>()
    val chosenEvent: LiveData<UiState<String?>> = _chosenEvent

    fun getChosenEvent() {
        _chosenEvent.value = UiState.Loading
        repository.getChosenEvent {_chosenEvent.value = it}
    }


    private val _addPlanProduct = MutableLiveData<UiState<PlanProductModel>>()
    val addPlanProduct: LiveData<UiState<PlanProductModel>> = _addPlanProduct

    fun addPlanProduct(event: String, planProduct: PlanProductModel) {
        _addPlanProduct.value = UiState.Loading
        repository.addPlanProduct(event, planProduct) {_addPlanProduct.value = it}
    }

    private val _updatePlanProduct = MutableLiveData<UiState<PlanProductModel>>()
    val updatePlanProduct: LiveData<UiState<PlanProductModel>> = _updatePlanProduct

    fun updatePlanProduct(event: String, planProduct: PlanProductModel) {
        _updatePlanProduct.value = UiState.Loading
        repository.updatePlanProduct(event, planProduct) {_updatePlanProduct.value = it}
    }

    private val _deletePlanProduct = MutableLiveData<UiState<PlanProductModel>>()
    val deletePlanProduct: LiveData<UiState<PlanProductModel>> = _deletePlanProduct

    fun deletePlanProduct(event: String, planProduct: PlanProductModel) {
        _deletePlanProduct.value = UiState.Loading
        repository.deletePlanProduct(event, planProduct) {_deletePlanProduct.value = it}
    }

    private val _planProducts = MutableLiveData<UiState<List<PlanProductModel>>>()
    val planProducts: LiveData<UiState<List<PlanProductModel>>> = _planProducts

    fun getPlanProducts(event: String) = viewModelScope.launch {
        _planProducts.value = UiState.Loading
        val result = repository.getPlanProducts(event)
        _planProducts.value = result
    }

    private val _addPurchase = MutableLiveData<UiState<PurchaseModel>>()
    val addPurchase: LiveData<UiState<PurchaseModel>> = _addPurchase

    fun addPurchase(event: String, purchase: PurchaseModel) {
        _addPurchase.value = UiState.Loading
        repository.addPurchase(event, purchase) {_addPurchase.value = it}
    }

    private val _updatePurchase = MutableLiveData<UiState<PurchaseModel>>()
    val updatePurchase: LiveData<UiState<PurchaseModel>> = _updatePurchase

    fun updatePurchase(event: String, purchase: PurchaseModel) {
        _updatePurchase.value = UiState.Loading
        repository.updatePurchase(event, purchase) {_updatePurchase.value = it}
    }

    private val _deletePurchase = MutableLiveData<UiState<PurchaseModel>>()
    val deletePurchase: LiveData<UiState<PurchaseModel>> = _deletePurchase

    fun deletePurchase(event: String, purchase: PurchaseModel) {
        _deletePurchase.value = UiState.Loading
        repository.deletePurchase(event, purchase) {_deletePurchase.value = it}
    }


    private val _purchases = MutableLiveData<UiState<List<PurchaseModel>>>()
    val purchases: LiveData<UiState<List<PurchaseModel>>> = _purchases

    fun getPurchases(event: String) = viewModelScope.launch {
        _purchases.value = UiState.Loading
        val result = repository.getPurchase(event)
        _purchases.value = result
    }
    fun onUploadCheck(fileUris: Uri, onResult: (UiState<Pair<Uri, String>>) -> Unit){
        onResult.invoke(UiState.Loading)
        viewModelScope.launch {
            repository.uploadCheck(fileUris,onResult)
        }
    }

}