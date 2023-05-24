package com.example.dacha.data.repository

import com.example.dacha.data.model.EventModel
import com.example.dacha.data.model.PersonModel
import com.example.dacha.data.model.PlanProductModel
import com.example.dacha.data.model.TransactionModel
import com.example.dacha.utils.FireDatabase
import com.example.dacha.utils.UiState
import com.google.firebase.database.FirebaseDatabase

class DebtRepositoryImpl(val database: FirebaseDatabase) : DebtRepository {
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

    override fun getPeople(result: (UiState<List<PersonModel>>) -> Unit) {
        val ref = database.reference.child(FireDatabase.PEOPLE)
        ref.get()
            .addOnSuccessListener {
                val people = arrayListOf<PersonModel>()
                for (item in it.children) {
                    val person = item.getValue(PersonModel::class.java)
                    if (person != null) people.add(person)
                }
                result.invoke(UiState.Success(people))
            }
    }

    override fun addTransaction(
        transaction: TransactionModel,
        result: (UiState<Pair<TransactionModel, String>>) -> Unit
    ) {
        val ref = database.reference.child(FireDatabase.TRANSACTIONS).push()
        val uniqueKey = ref.key ?: "invalid"
        transaction.key = uniqueKey
        ref
            .setValue(transaction)
            .addOnSuccessListener {
                result.invoke(UiState.Success(Pair(transaction, "Перевод успешно зафиксирован")))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun deleteTransaction(
        transaction: TransactionModel,
        result: (UiState<Pair<TransactionModel, String>>) -> Unit
    ) {
        val ref = database.reference.child(FireDatabase.TRANSACTIONS).child(transaction.key.toString())
        ref
            .removeValue()
            .addOnSuccessListener {
                result.invoke(UiState.Success(Pair(transaction, "Перевод успешно удален")))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun getTransactions(result: (UiState<List<TransactionModel>>) -> Unit) {
        val ref = database.reference.child(FireDatabase.TRANSACTIONS)
        ref.get()
            .addOnSuccessListener {
                val transactions = arrayListOf<TransactionModel>()
                for (item in it.children){
                    val transaction = item.getValue(TransactionModel::class.java)
                    if (transaction != null) transactions.add(transaction)
                }
                result.invoke(UiState.Success(transactions))
            }
    }
}