package com.example.dacha.data.repository

import com.example.dacha.data.model.TransactionModel
import com.example.dacha.utils.UiState

interface DebtRepository {
    fun addTransaction(transaction: TransactionModel, result: (UiState<TransactionModel>) -> Unit)
    fun deleteTransaction(transaction: TransactionModel, result: (UiState<TransactionModel>) -> Unit)
    suspend fun getTransactions(): UiState<List<TransactionModel>>
}