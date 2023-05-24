package com.example.dacha.data.repository

import com.example.dacha.data.model.EventModel
import com.example.dacha.data.model.PersonModel
import com.example.dacha.data.model.TransactionModel
import com.example.dacha.utils.UiState

interface DebtRepository {
    fun getEvents(result: (UiState<List<EventModel>>) -> Unit)
    fun getPeople(result: (UiState<List<PersonModel>>) -> Unit)
    fun addTransaction(transaction: TransactionModel, result: (UiState<Pair<TransactionModel, String>>) -> Unit)
    fun deleteTransaction(transaction: TransactionModel, result: (UiState<Pair<TransactionModel, String>>) -> Unit)
    fun getTransactions(result: (UiState<List<TransactionModel>>) -> Unit)
}