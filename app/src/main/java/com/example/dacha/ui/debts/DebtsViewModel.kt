package com.example.dacha.ui.debts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dacha.data.model.TransactionModel
import com.example.dacha.data.repository.DebtRepository
import com.example.dacha.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DebtsViewModel @Inject constructor(val repository: DebtRepository) : ViewModel() {

    private val _addTransaction = MutableLiveData<UiState<TransactionModel>>()
    val addTransaction: LiveData<UiState<TransactionModel>> = _addTransaction


    private val _deleteTransaction = MutableLiveData<UiState<TransactionModel>>()
    val deleteTransaction: LiveData<UiState<TransactionModel>> = _deleteTransaction

    private val _transactions = MutableLiveData<UiState<List<TransactionModel>>>()
    val transactions: LiveData<UiState<List<TransactionModel>>> = _transactions

    fun addTransaction(transaction: TransactionModel) {
        _addTransaction.value = UiState.Loading
        repository.addTransaction(transaction) {_addTransaction.value = it}
    }

    fun deleteTransaction(transaction: TransactionModel) {
        _deleteTransaction.value = UiState.Loading
        repository.deleteTransaction(transaction) {_deleteTransaction.value = it}
    }

    fun getTransactions() = viewModelScope.launch {
        _transactions.value = UiState.Loading
        val result = repository.getTransactions()
        _transactions.value = result
    }

}