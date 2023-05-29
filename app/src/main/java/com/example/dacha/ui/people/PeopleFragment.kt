package com.example.dacha.ui.people

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dacha.R
import com.example.dacha.data.model.NewsModel
import com.example.dacha.data.model.PersonModel
import com.example.dacha.data.model.TransactionModel
import com.example.dacha.databinding.FragmentPeopleBinding
import com.example.dacha.ui.debts.DebtsViewModel
import com.example.dacha.ui.home.HomeViewModel
import com.example.dacha.ui.products.CalendarBottomFragment
import com.example.dacha.ui.products.EventBottomFragment
import com.example.dacha.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime

@AndroidEntryPoint
class PeopleFragment : Fragment() {

    private val viewModel: PeopleViewModel by viewModels()
    private val debtsVM: DebtsViewModel by viewModels()
    val homeVM: HomeViewModel by viewModels()
    var deleteItemPos = -1
    lateinit var binding: FragmentPeopleBinding
    val adapter by lazy {
        PeopleAdapter(onDeleteClicked = { pos, item -> onDeleteClicked(pos, item) },
            onPersonClicked = { pos, item -> onPersonClicked(pos, item) })
    }
    private var transactions = mutableListOf<TransactionModel>()
    private var news = mutableListOf<NewsModel>()

    var person = PersonModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPeopleBinding.inflate(inflater, container, false)
        binding.peopleToolbar.inflateMenu(R.menu.info_menu)
        binding.peopleToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.transactions_info -> {
                    val showTransactions = mutableListOf<String>()
                    transactions.forEach {
                        showTransactions.add("${it.from?.name} перевел ${it.to?.name} ${it.howMuch}Р")
                    }
                    val transactionsDialog =
                        requireContext().createDialog(R.layout.transactions_dialog, true)
                    val listView = transactionsDialog.findViewById<ListView>(R.id.lv_transactions)
                    val transactionsAdapter = ArrayAdapter(
                        this.requireContext(),
                        R.layout.transaction_item,
                        showTransactions
                    )
                    listView.adapter = transactionsAdapter
                    listView.setOnItemClickListener { _, _, i, _ ->
                        val deleteDialog = requireContext().createDialog(R.layout.debt_dialog, true)
                        val tv = deleteDialog.findViewById<TextView>(R.id.tv_debt_top)
                        tv.text = "Не было такого?"
                        val btn = deleteDialog.findViewById<Button>(R.id.send_dialog_btn)
                        btn.text = "Удалить перевод"
                        btn.setOnClickListener {
                            debtsVM.deleteTransaction(transactions[i])
                            homeVM.addNews(
                                NewsModel(
                                    null,
                                    person,
                                    "Удалил(а) перевод ${transactions[i].from?.name} - ${transactions[i].to?.name} (${transactions[i].howMuch}P)",
                                    LocalDateTime.now().toString().split(".")[0]
                                )
                            )
                            transactionsAdapter.remove(showTransactions[i])
                            deleteDialog.dismiss()
                        }
                        deleteDialog.show()
                    }
                    val button =
                        transactionsDialog.findViewById<Button>(R.id.transactions_dialog_btn)
                    button?.setOnClickListener {
                        transactionsDialog.dismiss()
                    }
                    transactionsDialog.show()
                    true
                }
                R.id.history_info -> {
                    val showNews = mutableListOf<String>()
                    news.forEach {
                        showNews.add(
                            "${it.person?.name ?: "Нет имени"}: ${it.info}\n${
                                it.date?.split(
                                    "T"
                                )?.get(0)
                            } в ${it.date?.split("T")?.get(1)}"
                        )
                    }
                    val newsDialog =
                        requireContext().createDialog(R.layout.transactions_dialog, true)
                    val textView = newsDialog.findViewById<TextView>(R.id.tv_transactions_top)
                    textView.text = "История"
                    val listView = newsDialog.findViewById<ListView>(R.id.lv_transactions)
                    val transactionsAdapter = ArrayAdapter(
                        this.requireContext(),
                        R.layout.transaction_item,
                        showNews
                    )
                    listView.adapter = transactionsAdapter
                    val button = newsDialog.findViewById<Button>(R.id.transactions_dialog_btn)
                    button?.setOnClickListener {
                        newsDialog.dismiss()
                    }
                    newsDialog.show()
                    true
                }
                else -> false
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAdd.setOnClickListener {
            val personBottomFragment = PersonBottomFragment()
            personBottomFragment.setDismissListener {
                if (it) {
                    viewModel.getPeople()
                }
            }
            personBottomFragment.show(childFragmentManager, "add person")
        }

        binding.rcPeople.layoutManager = LinearLayoutManager(requireContext())
        binding.rcPeople.adapter = adapter
        homeVM.getPerson()
        homeVM.getNews()
        viewModel.getPeople()
        debtsVM.getTransactions()
        observer()
    }

    private fun observer() {
        debtsVM.transactions.observe(viewLifecycleOwner) { state ->
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
                    transactions = state.data.toMutableList()
                }
                else -> {
                    binding.progressBar.show()
                }
            }
        }
        homeVM.news.observe(viewLifecycleOwner) { state ->
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
                    news = state.data.toMutableList()
                }
                else -> {
                    binding.progressBar.show()
                }
            }
        }
        homeVM.person.observe(viewLifecycleOwner) { state ->
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
                    person = state.data!!
                }
                else -> {
                    binding.progressBar.show()
                }
            }
        }
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
                    adapter.updateList(state.data.toMutableList())
                }
                else -> {
                    binding.progressBar.show()
                }
            }
        }
        viewModel.deletePerson.observe(viewLifecycleOwner) { state ->
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
                    homeVM.addNews(
                        NewsModel(
                            null,
                            person,
                            "Удалил(а) ${state.data.first.name}",
                            LocalDateTime.now().toString().split(".")[0]
                        )
                    )
                    if (deleteItemPos > -1) {
                        toast(state.data.second)
                        adapter.removeItem(deleteItemPos)
                    }
                }
                else -> {
                    binding.progressBar.show()
                }
            }
        }
    }

    private fun onDeleteClicked(pos: Int, item: PersonModel) {
        deleteItemPos = pos
        viewModel.deletePerson(item)
    }

    private fun onPersonClicked(pos: Int, item: PersonModel) {
        val personBottomFragment = PersonBottomFragment(item)
        personBottomFragment.setDismissListener {
            if (it) {
                viewModel.getPeople()
            }
        }
        personBottomFragment.show(childFragmentManager, "change person")
    }
}
