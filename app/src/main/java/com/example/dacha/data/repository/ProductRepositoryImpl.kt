package com.example.dacha.data.repository


import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.example.dacha.data.model.*
import com.example.dacha.utils.FireDatabase
import com.example.dacha.utils.SharedPrefConstants
import com.example.dacha.utils.UiState
import com.google.firebase.FirebaseException
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ProductRepositoryImpl(
    val database: FirebaseDatabase,
    val shPref: SharedPreferences,
    val storageReference: StorageReference
) :
    ProductRepository {
    override fun addEvent(event: EventModel, result: (UiState<Pair<EventModel, String>>) -> Unit) {
        val ref = database.reference.child(FireDatabase.EVENTS).push()
        val uniqueKey = ref.key ?: "invalid"
        val newEvent = EventModel(
            EventInfo(
                eDate = event.eInfo?.eDate,
                eKey = uniqueKey,
                eName = event.eInfo?.eName
            ),
            ePeople = event.ePeople,
            ePlanProducts = event.ePlanProducts,
            ePurchases = event.ePurchases
        )
        ref
            .setValue(newEvent)
            .addOnSuccessListener {
                result.invoke(UiState.Success(Pair(newEvent, "Поездка успешно добавлена")))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun updateEvent(
        event: EventModel,
        result: (UiState<Pair<EventModel, String>>) -> Unit
    ) {
        val ref = database.reference.child(FireDatabase.EVENTS).child(event.eInfo?.eKey.toString())
        ref.child("einfo")
            .setValue(EventInfo(event.eInfo?.eDate, event.eInfo?.eKey, event.eInfo?.eName))
        ref.child("epeople").setValue(event.ePeople)
        result.invoke(UiState.Success(Pair(event, "Поездка успешно обновлена")))
    }

    override fun deleteEvent(
        event: EventModel,
        result: (UiState<Pair<EventModel, String>>) -> Unit
    ) {
        val ref = database.reference.child(FireDatabase.EVENTS).child(event.eInfo?.eKey.toString())
        ref
            .removeValue()
            .addOnSuccessListener {
                result.invoke(UiState.Success(Pair(event, "Поездка успешно удалена")))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun chooseEvent(
        event: EventModel,
        result: (UiState<Pair<EventModel, String>>) -> Unit
    ) {
        shPref.edit().putString(SharedPrefConstants.EVENT_ID, event.eInfo?.eKey).apply()
        result.invoke(UiState.Success(Pair(event, "Поездка выбрана")))
    }

    override fun getEvents(result: (UiState<List<EventModel>>) -> Unit) {
        val ref = database.reference.child(FireDatabase.EVENTS)
        ref.get()
            .addOnSuccessListener {
                val events = arrayListOf<EventModel>()
                for (item in it.children) {
                    val event = item.getValue(EventModel::class.java)
                    if (event != null) events.add(event)
                }
                result.invoke(UiState.Success(events))
            }
    }

    override fun getChosenEvent(result: (UiState<String?>) -> Unit) {
        val id = shPref.getString(SharedPrefConstants.EVENT_ID, null)
        if (id == null) {
            result.invoke(UiState.Success(null))
        } else {
            result.invoke(UiState.Success(id))
        }
    }

    override fun addPlanProduct(
        event: String,
        planProduct: PlanProductModel,
        result: (UiState<Pair<PlanProductModel, String>>) -> Unit
    ) {
        val ref = database.reference.child(FireDatabase.EVENTS).child(event)
            .child(FireDatabase.PLAN_PRODUCT).push()
        val uniqueKey = ref.key ?: "invalid"
        val newPlanProduct = PlanProductModel(
            pAmount = planProduct.pAmount,
            pKey = uniqueKey,
            pProduct = planProduct.pProduct,
            pWhose = planProduct.pWhose
        )
        ref
            .setValue(newPlanProduct)
            .addOnSuccessListener {
                result.invoke(UiState.Success(Pair(newPlanProduct, "Продукт успешно добавлен")))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun updatePlanProduct(
        event: String,
        planProduct: PlanProductModel,
        result: (UiState<Pair<PlanProductModel, String>>) -> Unit
    ) {
        val ref = database.reference.child(FireDatabase.EVENTS).child(event)
            .child(FireDatabase.PLAN_PRODUCT).child(planProduct.pKey.toString())
        ref
            .setValue(planProduct)
            .addOnSuccessListener {
                result.invoke(UiState.Success(Pair(planProduct, "Продукт успешно обновлена")))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun deletePlanProduct(
        event: String,
        planProduct: PlanProductModel,
        result: (UiState<Pair<PlanProductModel, String>>) -> Unit
    ) {
        val ref = database.reference.child(FireDatabase.EVENTS).child(event)
            .child(FireDatabase.PLAN_PRODUCT).child(planProduct.pKey.toString())
        ref
            .removeValue()
            .addOnSuccessListener {
                result.invoke(UiState.Success(Pair(planProduct, "Продукт успешно удалена")))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun getPlanProducts(event: String, result: (UiState<List<PlanProductModel>>) -> Unit) {
        val ref = database.reference.child(FireDatabase.EVENTS).child(event)
            .child(FireDatabase.PLAN_PRODUCT)
        ref.get()
            .addOnSuccessListener {
                val planProducts = arrayListOf<PlanProductModel>()
                for (item in it.children) {
                    val event = item.getValue(PlanProductModel::class.java)
                    if (event != null) planProducts.add(event)
                }
                result.invoke(UiState.Success(planProducts))
            }
    }

    override fun addPurchase(
        event: String,
        purchase: PurchaseModel,
        result: (UiState<Pair<PurchaseModel, String>>) -> Unit
    ) {
        val ref = database.reference.child(FireDatabase.EVENTS).child(event)
            .child(FireDatabase.PURCHASE).push()
        val uniqueKey = ref.key ?: "invalid"
        val newPurchase = PurchaseModel(
            purchaseInfo = (PurchaseInfo(
                key = uniqueKey,
                market = purchase.purchaseInfo?.market,
                photo = purchase.purchaseInfo?.photo,
                paid = purchase.purchaseInfo?.paid
            )),
            resultProducts = purchase.resultProducts
        )
        ref
            .setValue(newPurchase)
            .addOnSuccessListener {
                result.invoke(UiState.Success(Pair(newPurchase, "Покупка успешно добавлена")))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun updatePurchase(
        event: String,
        purchase: PurchaseModel,
        result: (UiState<Pair<PurchaseModel, String>>) -> Unit
    ) {
        val ref = database.reference.child(FireDatabase.EVENTS).child(event)
            .child(FireDatabase.PURCHASE).child(purchase.purchaseInfo?.key.toString())
        ref
            .setValue(purchase)
            .addOnSuccessListener {
                result.invoke(UiState.Success(Pair(purchase, "Покупка успешно обновлена")))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun deletePurchase(
        event: String,
        purchase: PurchaseModel,
        result: (UiState<Pair<PurchaseModel, String>>) -> Unit
    ) {
        val ref = database.reference.child(FireDatabase.EVENTS).child(event)
            .child(FireDatabase.PURCHASE).child(purchase.purchaseInfo?.key.toString())
        ref
            .removeValue()
            .addOnSuccessListener {
                result.invoke(UiState.Success(Pair(purchase, "Покупка успешно удалена")))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun getPurchase(event: String, result: (UiState<List<PurchaseModel>>) -> Unit) {
        val ref = database.reference.child(FireDatabase.EVENTS).child(event)
            .child(FireDatabase.PURCHASE)
        ref.get()
            .addOnSuccessListener {
                val purchases = arrayListOf<PurchaseModel>()
                for (item in it.children) {
                    val purchase = item.getValue(PurchaseModel::class.java)
                    if (purchase != null) purchases.add(purchase)
                }
                result.invoke(UiState.Success(purchases))
            }
    }

    override suspend fun uploadCheck(fileUri: Uri, onResult: (UiState<Pair<Uri, String>>) -> Unit) {
        try {
            var url = ""
            val uri: Uri = withContext(Dispatchers.IO) {
                storageReference
                    .child(fileUri.toString())
                    .putFile(fileUri)
                    .await()
                    .storage
                    .downloadUrl
                    .addOnSuccessListener {
                        url = it.toString()
                    }
                    .await()
            }
            onResult.invoke(UiState.Success(Pair(uri, url)))
        } catch (e: FirebaseException) {
            onResult.invoke(UiState.Failure(e.message))
        } catch (e: Exception) {
            onResult.invoke(UiState.Failure(e.message))
        }
    }

}