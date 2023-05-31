package com.example.dacha.data.model

data class DebtPresentation(
    val name: String,
    val needToSend: List<DebtModel>,
    val needToGet: List<Pair<String, DebtModel>>,
    val paid: MutableMap<String, MutableList<String>>,
    val bought: MutableMap<String, MutableList<String>>
)
