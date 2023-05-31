package com.example.dacha.data.repository

import com.example.dacha.data.model.*
import com.example.dacha.utils.FireDatabase
import com.example.dacha.utils.UiState
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class DebtRepositoryImpl(val database: FirebaseDatabase) : DebtRepository {


    override fun addTransaction(
        transaction: TransactionModel,
        result: (UiState<TransactionModel>) -> Unit
    ) {
        val ref = database.reference.child(FireDatabase.TRANSACTIONS).push()
        val uniqueKey = ref.key ?: "invalid"
        transaction.key = uniqueKey
        ref
            .setValue(transaction)
            .addOnSuccessListener {
                result(UiState.Success(transaction))
            }
            .addOnFailureListener {
                result(UiState.Failure(it.localizedMessage))
            }
    }

    override fun deleteTransaction(
        transaction: TransactionModel,
        result: (UiState<TransactionModel>) -> Unit
    ) {
        val ref =
            database.reference.child(FireDatabase.TRANSACTIONS).child(transaction.key.toString())
        ref
            .removeValue()
            .addOnSuccessListener {
                result(UiState.Success(transaction))
            }
            .addOnFailureListener {
                result(UiState.Failure(it.localizedMessage))
            }
    }

    override suspend fun getTransactions() : UiState<List<TransactionModel>> {
        val ref = database.reference.child(FireDatabase.TRANSACTIONS)
        val transactions = arrayListOf<TransactionModel>()
        ref.get()
            .addOnSuccessListener {
                for (item in it.children) {
                    val transaction = item.getValue(TransactionModel::class.java)
                    if (transaction != null) transactions.add(transaction)
                }
            }.await()
        return UiState.Success(transactions)
    }
}