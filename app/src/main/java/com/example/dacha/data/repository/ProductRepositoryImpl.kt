package com.example.dacha.data.repository


import android.content.SharedPreferences
import android.net.Uri
import com.example.dacha.data.model.*
import com.example.dacha.utils.FireDatabase
import com.example.dacha.utils.SharedPrefConstants
import com.example.dacha.utils.UiState
import com.google.firebase.FirebaseException
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ProductRepositoryImpl(
    val database: FirebaseDatabase,
    private val shPref: SharedPreferences,
    private val storageReference: StorageReference
) :
    ProductRepository {
    override fun addEvent(event: EventModel, result: (UiState<EventModel>) -> Unit) {
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
                result(UiState.Success(newEvent))
            }
            .addOnFailureListener {
                result(UiState.Failure(it.localizedMessage))
            }
    }

    override fun updateEvent(
        event: EventModel,
        result: (UiState<EventModel>) -> Unit
    ) {
        val ref = database.reference.child(FireDatabase.EVENTS).child(event.eInfo?.eKey.toString())
        ref.child("einfo")
            .setValue(EventInfo(event.eInfo?.eDate, event.eInfo?.eKey, event.eInfo?.eName))
        ref.child("epeople").setValue(event.ePeople)
        result(UiState.Success(event))
    }

    override fun deleteEvent(
        event: EventModel,
        result: (UiState<EventModel>) -> Unit
    ) {
        val ref = database.reference.child(FireDatabase.EVENTS).child(event.eInfo?.eKey.toString())
        ref
            .removeValue()
            .addOnSuccessListener {
                result(UiState.Success(event))
            }
            .addOnFailureListener {
                result(UiState.Failure(it.localizedMessage))
            }
    }

    override fun chooseEvent(
        event: EventModel,
        result: (UiState<EventModel>) -> Unit
    ) {
        shPref.edit().putString(SharedPrefConstants.EVENT_ID, event.eInfo?.eKey).apply()
        result(UiState.Success(event))
    }

    override suspend fun getEvents(): UiState<List<EventModel>> {
        val ref = database.reference.child(FireDatabase.EVENTS)
        val events = arrayListOf<EventModel>()
        ref.get()
            .addOnSuccessListener {
                for (item in it.children) {
                    val event = item.getValue(EventModel::class.java)
                    if (event != null) events.add(event)
                }
            }.await()
        return UiState.Success(events)
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
        result: (UiState<PlanProductModel>) -> Unit
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
                result(UiState.Success(newPlanProduct))
            }
            .addOnFailureListener {
                result(UiState.Failure(it.localizedMessage))
            }
    }

    override fun updatePlanProduct(
        event: String,
        planProduct: PlanProductModel,
        result: (UiState<PlanProductModel>) -> Unit
    ) {
        val ref = database.reference.child(FireDatabase.EVENTS).child(event)
            .child(FireDatabase.PLAN_PRODUCT).child(planProduct.pKey.toString())
        ref
            .setValue(planProduct)
            .addOnSuccessListener {
                result(UiState.Success(planProduct))
            }
            .addOnFailureListener {
                result(UiState.Failure(it.localizedMessage))
            }
    }

    override fun deletePlanProduct(
        event: String,
        planProduct: PlanProductModel,
        result: (UiState<PlanProductModel>) -> Unit
    ) {
        val ref = database.reference.child(FireDatabase.EVENTS).child(event)
            .child(FireDatabase.PLAN_PRODUCT).child(planProduct.pKey.toString())
        ref
            .removeValue()
            .addOnSuccessListener {
                result(UiState.Success(planProduct))
            }
            .addOnFailureListener {
                result(UiState.Failure(it.localizedMessage))
            }
    }

    override suspend fun getPlanProducts(event: String): UiState<List<PlanProductModel>> {
        val ref = database.reference.child(FireDatabase.EVENTS).child(event)
            .child(FireDatabase.PLAN_PRODUCT)
        val planProducts = arrayListOf<PlanProductModel>()
        ref.get()
            .addOnSuccessListener {
                for (item in it.children) {
                    val planProduct = item.getValue(PlanProductModel::class.java)
                    if (planProduct != null) planProducts.add(planProduct)
                }
            }.await()
        return UiState.Success(planProducts)
    }

    override fun addPurchase(
        event: String,
        purchase: PurchaseModel,
        result: (UiState<PurchaseModel>) -> Unit
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
                result(UiState.Success(newPurchase))
            }
            .addOnFailureListener {
                result(UiState.Failure(it.localizedMessage))
            }
    }

    override fun updatePurchase(
        event: String,
        purchase: PurchaseModel,
        result: (UiState<PurchaseModel>) -> Unit
    ) {
        val ref = database.reference.child(FireDatabase.EVENTS).child(event)
            .child(FireDatabase.PURCHASE).child(purchase.purchaseInfo?.key.toString())
        ref
            .setValue(purchase)
            .addOnSuccessListener {
                result(UiState.Success(purchase))
            }
            .addOnFailureListener {
                result(UiState.Failure(it.localizedMessage))
            }
    }

    override fun deletePurchase(
        event: String,
        purchase: PurchaseModel,
        result: (UiState<PurchaseModel>) -> Unit
    ) {
        val ref = database.reference.child(FireDatabase.EVENTS).child(event)
            .child(FireDatabase.PURCHASE).child(purchase.purchaseInfo?.key.toString())
        ref
            .removeValue()
            .addOnSuccessListener {
                result(UiState.Success(purchase))
            }
            .addOnFailureListener {
                result(UiState.Failure(it.localizedMessage))
            }
    }

    override suspend fun getPurchase(event: String): UiState<List<PurchaseModel>> {
        val ref = database.reference.child(FireDatabase.EVENTS).child(event)
            .child(FireDatabase.PURCHASE)
        val purchases = arrayListOf<PurchaseModel>()
        ref.get()
            .addOnSuccessListener {
                for (item in it.children) {
                    val purchase = item.getValue(PurchaseModel::class.java)
                    if (purchase != null) purchases.add(purchase)
                }
            }.await()
        return UiState.Success(purchases)
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
            onResult(UiState.Success(Pair(uri, url)))
        } catch (e: FirebaseException) {
            onResult(UiState.Failure(e.message))
        } catch (e: Exception) {
            onResult(UiState.Failure(e.message))
        }
    }

}