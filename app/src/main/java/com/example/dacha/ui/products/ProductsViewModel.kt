package com.example.dacha.ui.products

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dacha.data.model.EventModel
import com.example.dacha.data.model.PersonModel
import com.example.dacha.data.model.PlanProductModel
import com.example.dacha.data.model.PurchaseModel
import com.example.dacha.data.repository.ProductRepository
import com.example.dacha.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(val repository: ProductRepository) : ViewModel() {
    private val _addEvent = MutableLiveData<UiState<Pair<EventModel, String>>>()
    val addEvent: LiveData<UiState<Pair<EventModel, String>>> = _addEvent

    fun addEvent(event: EventModel) {
        _addEvent.value = UiState.Loading
        repository.addEvent(event) {_addEvent.value = it}
    }

    private val _updateEvent = MutableLiveData<UiState<Pair<EventModel, String>>>()
    val updateEvent: LiveData<UiState<Pair<EventModel, String>>> = _updateEvent

    fun updateEvent(event: EventModel) {
        _updateEvent.value = UiState.Loading
        repository.updateEvent(event) {_updateEvent.value = it}
    }

    private val _deleteEvent = MutableLiveData<UiState<Pair<EventModel, String>>>()
    val deleteEvent: LiveData<UiState<Pair<EventModel, String>>> = _deleteEvent

    fun deleteEvent(event: EventModel) {
        _deleteEvent.value = UiState.Loading
        repository.deleteEvent(event) {_deleteEvent.value = it}
    }

    private val _events = MutableLiveData<UiState<List<EventModel>>>()
    val events: LiveData<UiState<List<EventModel>>> = _events

    fun getEvents() {
        _events.value = UiState.Loading
        repository.getEvents {_events.value = it}
    }

    private val _chooseEvent = MutableLiveData<UiState<Pair<EventModel, String>>>()
    val chooseEvent: LiveData<UiState<Pair<EventModel, String>>> = _chooseEvent

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


    private val _addPlanProduct = MutableLiveData<UiState<Pair<PlanProductModel, String>>>()
    val addPlanProduct: LiveData<UiState<Pair<PlanProductModel, String>>> = _addPlanProduct

    fun addPlanProduct(event: String, planProduct: PlanProductModel) {
        _addPlanProduct.value = UiState.Loading
        repository.addPlanProduct(event, planProduct) {_addPlanProduct.value = it}
    }

    private val _updatePlanProduct = MutableLiveData<UiState<Pair<PlanProductModel, String>>>()
    val updatePlanProduct: LiveData<UiState<Pair<PlanProductModel, String>>> = _updatePlanProduct

    fun updatePlanProduct(event: String, planProduct: PlanProductModel) {
        _updatePlanProduct.value = UiState.Loading
        repository.updatePlanProduct(event, planProduct) {_updatePlanProduct.value = it}
    }

    private val _deletePlanProduct = MutableLiveData<UiState<Pair<PlanProductModel, String>>>()
    val deletePlanProduct: LiveData<UiState<Pair<PlanProductModel, String>>> = _deletePlanProduct

    fun deletePlanProduct(event: String, planProduct: PlanProductModel) {
        _deletePlanProduct.value = UiState.Loading
        repository.deletePlanProduct(event, planProduct) {_deletePlanProduct.value = it}
    }

    private val _planProducts = MutableLiveData<UiState<List<PlanProductModel>>>()
    val planProducts: LiveData<UiState<List<PlanProductModel>>> = _planProducts

    fun getPlanProducts(event: String) {
        _planProducts.value = UiState.Loading
        repository.getPlanProducts(event) {_planProducts.value = it}
    }

    private val _addPurchase = MutableLiveData<UiState<Pair<PurchaseModel, String>>>()
    val addPurchase: LiveData<UiState<Pair<PurchaseModel, String>>> = _addPurchase

    fun addPurchase(event: String, purchase: PurchaseModel) {
        _addPurchase.value = UiState.Loading
        repository.addPurchase(event, purchase) {_addPurchase.value = it}
    }

    private val _updatePurchase = MutableLiveData<UiState<Pair<PurchaseModel, String>>>()
    val updatePurchase: LiveData<UiState<Pair<PurchaseModel, String>>> = _updatePurchase

    fun updatePurchase(event: String, purchase: PurchaseModel) {
        _updatePurchase.value = UiState.Loading
        repository.updatePurchase(event, purchase) {_updatePurchase.value = it}
    }

    private val _deletePurchase = MutableLiveData<UiState<Pair<PurchaseModel, String>>>()
    val deletePurchase: LiveData<UiState<Pair<PurchaseModel, String>>> = _deletePurchase

    fun deletePurchase(event: String, purchase: PurchaseModel) {
        _deletePurchase.value = UiState.Loading
        repository.deletePurchase(event, purchase) {_deletePurchase.value = it}
    }


    private val _purchases = MutableLiveData<UiState<List<PurchaseModel>>>()
    val purchases: LiveData<UiState<List<PurchaseModel>>> = _purchases

    fun getPurchases(event: String) {
        _purchases.value = UiState.Loading
        repository.getPurchase(event) {_purchases.value = it}
    }
    fun onUploadCheck(fileUris: Uri, onResult: (UiState<Pair<Uri, String>>) -> Unit){
        onResult.invoke(UiState.Loading)
        viewModelScope.launch {
            repository.uploadCheck(fileUris,onResult)
        }
    }

}