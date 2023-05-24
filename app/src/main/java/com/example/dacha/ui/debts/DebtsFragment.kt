package com.example.dacha.ui.debts

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dacha.R
import com.example.dacha.data.model.*
import com.example.dacha.databinding.FragmentDebtsBinding
import com.example.dacha.ui.products.PlanProductAdapter
import com.example.dacha.ui.products.PlanProductBottomFragment
import com.example.dacha.utils.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DebtsFragment : Fragment() {


    val viewModel: DebtsViewModel by viewModels()
    lateinit var binding: FragmentDebtsBinding
    var events = listOf<EventModel>()
    var people = listOf<PersonModel>()
    var transfers = mutableMapOf<String, MutableList<DebtModel>>()
    val boughtProducts = mutableMapOf<String, MutableMap<String, MutableList<String>>>()
    val paidProducts = mutableMapOf<String, MutableMap<String, MutableList<String>>>()
    var isSuccessAdd = false
    val adapter by lazy {
        DebtsViewPagerAdapter(onDebtClicked = { pos, name ->
            onDebtClicked(
                pos,
                name
            )
        }, onDebtLongClicked = { pos, name, debt -> onDebtLongClicked(pos, name, debt) })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDebtsBinding.inflate(inflater, container, false)
        viewModel.getPeople()
        viewModel.getEvents()
        viewModel.getTransactions()
//        val adapter = DebtsViewPagerAdapter(mapOfInAll, mapOfProducts)
//
//
//        binding.dViewPager.adapter = adapter
//        TabLayoutMediator(binding.dTabLayout, binding.dViewPager) { tab, position ->
//            tab.text = mapOfInAll.keys.toList()[position]
//        }.attach()
//        viewModel.updateAdapter(
//            Pair(
//                DebtsViewPagerAdapter(mapOfInAll, mapOfProducts),
//                mapOfInAll.keys.toList()
//            )
//        )
//        adapter.notifyDataSetChanged()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dViewPager.adapter = adapter

        observer()
    }

    private fun observer() {
        viewModel.people.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progressBar.show()
                }
                is UiState.Failure -> {
                    binding.progressBar.hide()
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.progressBar.hide()
                    people = state.data
                }
                else -> {
                    binding.progressBar.show()
                }
            }
        }
        viewModel.events.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progressBar.show()
                }
                is UiState.Failure -> {
                    binding.progressBar.hide()
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.progressBar.hide()
                    events = state.data
                    countDebts(events)
                    adapter.updateEvents(events)
                }
                else -> {
                    binding.progressBar.show()
                }
            }
        }
        viewModel.transactions.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progressBar.show()
                }
                is UiState.Failure -> {
                    binding.progressBar.hide()
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.progressBar.hide()
                    adapter.updateTransactions(state.data)
                }
                else -> {
                    binding.progressBar.show()
                }
            }
        }
    }

    private fun onDebtClicked(pos: Int, name: String) {
        people.forEach {
            if (name == it.name) {
                val clipboard = context?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Номер", it.number)
                clipboard.setPrimaryClip(clip)
                toast("Банк: ${it.bank}\nНомер скопирован")
            }
        }
    }

    private fun onDebtLongClicked(pos: Int, name: String, debt: DebtModel) {
        var from = PersonModel()
        var to = PersonModel()
        people.forEach {
            if (it.name == name) from = it
            else if (it.name == debt.dPerson) to = it
        }
        showDialog(TransactionModel(from, to, debt.dAmount.toInt(), null))
    }

    private fun showDialog(transaction: TransactionModel) {
        val dialog = requireContext().createDialog(R.layout.debt_dialog, true)
        val button = dialog.findViewById<MaterialButton>(R.id.send_dialog_btn)

        button.setOnClickListener {
            viewModel.addTransaction(transaction = transaction)
            viewModel.getTransactions()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun countDebts(events: List<EventModel>) {
        val rawProducts =
            mutableMapOf<String, MutableMap<String, MutableList<ResultProductModel>>>()
        for (event in events) {
            val resultProducts = mutableMapOf<String, MutableList<ResultProductModel>>()
            val eventPaid = mutableMapOf<String, MutableList<String>>()
            event.ePurchases?.forEach {
                resultProducts[it.value.purchaseInfo?.paid?.name.toString()] =
                    it.value.resultProducts?.values?.toMutableList()!!
                it.value.resultProducts!!.values.forEach { resPr ->
                    if (it.value.purchaseInfo?.paid?.name.toString() in eventPaid.keys) {
                        eventPaid[it.value.purchaseInfo?.paid?.name.toString()]?.add(
                            "${resPr.rProduct.toString()} ${
                                (resPr.pPrice?.times(
                                    resPr.rAmount!!
                                ))?.toInt().toString()
                            } P"
                        )
                    } else eventPaid[it.value.purchaseInfo?.paid?.name.toString()] =
                        mutableListOf(
                            "${resPr.rProduct.toString()} ${
                                (resPr.pPrice?.times(
                                    resPr.rAmount!!
                                ))?.toInt().toString()
                            } P"
                        )
                }
            }
            paidProducts[event.eInfo?.eKey.toString()] = eventPaid
            rawProducts[event.eInfo?.eKey.toString()] = resultProducts
        }
        val debts = mutableMapOf<String, MutableMap<String, MutableMap<String, Double>>>()
        val backMoney = mutableMapOf<String, MutableMap<String, Pair<Double, Double>>>()
        val transfers = mutableMapOf<String, MutableList<DebtModel>>()
        rawProducts.forEach { event ->
            val eventDebts = mutableMapOf<String, MutableMap<String, Double>>()
            val eventBackMoney = mutableMapOf<String, Pair<Double, Double>>()
            val eventBought = mutableMapOf<String, MutableList<String>>()
            event.value.forEach { purchase ->
                val purchaseDebts = mutableMapOf<String, Double>()
                purchase.value.forEach {
                    val price: Double
                    if (it.pPrice != null && it.rAmount != null && it.rWhose != null) {
                        price = it.pPrice!! * it.rAmount!! / it.rWhose!!.size

                        it.rWhose!!.forEach { person ->
                            if (person.name in eventBought.keys) eventBought[person.name]?.add("${it.rProduct.toString()} ${price.toInt()} P")
                            else eventBought[person.name.toString()] =
                                mutableListOf("${it.rProduct.toString()} ${price.toInt()} P")

                            eventBackMoney[person.name.toString()] = Pair(0.0, 0.0)
                            if (person.name == purchase.key) return@forEach
                            if (person.name in purchaseDebts.keys) purchaseDebts[person.name.toString()] =
                                purchaseDebts[person.name.toString()]!! + price
                            else purchaseDebts[person.name.toString()] = price
                        }
                    }
                }
                eventDebts[purchase.key] = purchaseDebts
                eventBackMoney[purchase.key] = Pair(0.0, 0.0)
            }
            boughtProducts[event.key] = eventBought
            backMoney[event.key] = eventBackMoney
            debts[event.key] = eventDebts
        }
        debts.forEach { event ->
            var eventBackMoney = backMoney[event.key] as MutableMap<String, Pair<Double, Double>>
            var payers = mutableListOf<Pair<String, Double>>()
            event.value.forEach { (payer, mutableMap) ->
                var fullMoney = 0.0

                mutableMap.forEach { (name, money) ->
                    val oldPair = eventBackMoney[name]
                    if (oldPair != null) {
                        if (oldPair.first > money) eventBackMoney[name] =
                            Pair(oldPair.first - money, oldPair.second)
                        else if (oldPair.first > 0.0) eventBackMoney[name] =
                            Pair(0.0, oldPair.second + money - oldPair.first)
                        else eventBackMoney[name] = Pair(oldPair.first, oldPair.second + money)
                    }
                    fullMoney += money
                }
                val oldPair = backMoney[event.key]?.get(payer)
                if (oldPair != null) {
                    if (oldPair.second >= fullMoney) eventBackMoney[payer] =
                        Pair(0.0, oldPair.second - fullMoney)
                    else eventBackMoney[payer] = Pair(fullMoney - oldPair.second, 0.0)
                }
            }
            eventBackMoney =
                eventBackMoney.toList().sortedBy { it.second.second }.toMap().toMutableMap()
            eventBackMoney.forEach { (name, pair) ->
                if (pair.first > 0) payers.add(Pair(name, pair.first))
                else return@forEach
            }
            payers = payers.sortedBy { it.second }.reversed().toMutableList()
            var i = 0
            for (debt in eventBackMoney.keys.reversed()) {
                if (eventBackMoney[debt]?.first == 0.0) {
                    if (debt in transfers.keys) transfers[debt]?.add(
                        (DebtModel(
                            eventBackMoney[debt]?.second ?: 0.0, payers[i].first
                        ))
                    )
                    else transfers[debt] =
                        mutableListOf(
                            DebtModel(
                                eventBackMoney[debt]?.second ?: 0.0,
                                payers[i].first
                            )
                        )
                    if (payers[i].second > (eventBackMoney[debt]?.second ?: 0.0)) {
                        payers[i] =
                            Pair(payers[i].first, payers[i].second - eventBackMoney[debt]?.second!!)

                    } else {
                        if ((i + 1) != payers.size) {
                            if (payers[i].first in transfers.keys) transfers[debt]?.add(
                                (DebtModel(
                                    (eventBackMoney[debt]?.second ?: 0.0) - payers[i].second,
                                    payers[i + 1].first
                                ))
                            )
                            else transfers[payers[i].first] =
                                mutableListOf(
                                    (DebtModel(
                                        (eventBackMoney[debt]?.second ?: 0.0) - payers[i].second,
                                        payers[i + 1].first
                                    ))
                                )
                            payers[i + 1] = Pair(
                                payers[i + 1].first,
                                payers[i + 1].second - ((eventBackMoney[debt]?.second
                                    ?: 0.0) - payers[i].second)
                            )
                            i++
                        }
                    }
                }
            }
            backMoney[event.key] = eventBackMoney
        }
        this.transfers = transfers
        updateUI(transfers, paidProducts, boughtProducts, people, events)
    }

    private fun updateUI(
        transfers: MutableMap<String, MutableList<DebtModel>>,
        paidProducts: MutableMap<String, MutableMap<String, MutableList<String>>>,
        boughtProducts: MutableMap<String, MutableMap<String, MutableList<String>>>,
        people: List<PersonModel>,
        events: List<EventModel>
    ) {
        val result = mutableListOf<DebtPresentation>()
        people.forEach {
            val name = it.name.toString()
            var itNeedToSend = mutableListOf<DebtModel>()
            val itNeedToGet = mutableListOf<Pair<String, DebtModel>>()
            val itPaid = mutableMapOf<String, MutableList<String>>()
            val itBought = mutableMapOf<String, MutableList<String>>()
            transfers.forEach { (s, debtModels) ->
                if (s == name) itNeedToSend = debtModels
                else {
                    debtModels.forEach { debt ->
                        if (debt.dPerson == name) itNeedToGet.add(Pair(s, debt))
                    }
                }
            }
            paidProducts.forEach { (event, eventPaidProducts) ->
                eventPaidProducts.forEach { (ppName, products) ->
                    if (ppName == name) {
                        if (event in itPaid.keys) itPaid[event]?.addAll(products)
                        else itPaid[event] = products
                    }
                }
            }
            boughtProducts.forEach { (event, eventBoughtProducts) ->
                eventBoughtProducts.forEach { (bpName, products) ->
                    if (bpName == name) {
                        if (event in itBought.keys) itBought[event]?.addAll(products)
                        else itBought[event] = products
                    }
                }
            }
            result.add(DebtPresentation(name, itNeedToSend, itNeedToGet, itPaid, itBought))
        }
        //binding.tvDebtTest.text = result.toString()
        TabLayoutMediator(binding.dTabLayout, binding.dViewPager) { tab, position ->
            tab.text = result[position].name
        }.attach()
        adapter.updateDebts(result)
    }
}