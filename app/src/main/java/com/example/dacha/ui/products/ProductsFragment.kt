package com.example.dacha.ui.products


import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dacha.R
import com.example.dacha.data.model.*
import com.example.dacha.databinding.FragmentProductsBinding
import com.example.dacha.utils.UiState
import com.example.dacha.utils.hide
import com.example.dacha.utils.show
import com.example.dacha.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsFragment : Fragment() {

    val viewModel: ProductsViewModel by viewModels()
    lateinit var binding: FragmentProductsBinding
    val adapter by lazy {
        PlanProductAdapter(onProductClicked = { pos, item -> onProductClicked(pos, item) })
    }

    val purchaseAdapter by lazy {
        PurchaseAdapter(onPurchaseClicked = { pos, item -> onPurchaseClicked(pos, item) })
    }

    private var chosenEventId: String? = null
    private var chosenEvent: EventModel? = null
    var events = listOf<EventModel>()
    var planProducts = mutableListOf<PlanProductModel>()
    var purchases = mutableListOf<PurchaseModel>()
    private var list: List<SimplePersonModel> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getPeople()
        viewModel.getEvents()
        binding = FragmentProductsBinding.inflate(inflater, container, false)
        binding.productsToolbar.inflateMenu(R.menu.event_menu)
        binding.productsToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.addEvent -> {
                    val eventBottomFragment = EventBottomFragment(null, false, list)
                    eventBottomFragment.setDismissListener {
                        if (it) {
                            viewModel.getEvents()
                            viewModel.getChosenEvent()
                        }
                    }
                    eventBottomFragment.show(childFragmentManager, "add event")
                    true
                }
                R.id.chooseEvent -> {
                    val calendarBottomFragment = CalendarBottomFragment(events, chosenEvent)
                    calendarBottomFragment.setDismissListener {
                        if (it) {
                            viewModel.getChosenEvent()
                            updateUI()
                        }
                    }
                    calendarBottomFragment.show(childFragmentManager, "choose event")
                    true
                }
                R.id.changeEvent -> {
                    val eventBottomFragment = EventBottomFragment(
                        chosenEvent, false, list
                    )
                    eventBottomFragment.setDismissListener {
                        if (it) {
                            viewModel.getEvents()
                            viewModel.getChosenEvent()
                        }
                    }
                    eventBottomFragment.show(childFragmentManager, "change event")
                    true
                }
                R.id.deleteEvent -> {
                    val eventBottomFragment = EventBottomFragment(chosenEvent, true, list)
                    eventBottomFragment.setDismissListener {
                        if (it) {
                            viewModel.getEvents()
                        }
                    }
                    eventBottomFragment.show(childFragmentManager, "delete event")
                    true
                }
                else -> false
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tbgChooseProducts.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.btnPlanProducts -> {
                        binding.layoutPlanProducts.show()
                        binding.layoutResultProducts.hide()
                    }
                    R.id.btnResultProducts -> {
                        binding.layoutPlanProducts.hide()
                        binding.layoutResultProducts.show()
                    }
                }
            }
        }

        binding.btnAddPlanProduct.setOnClickListener {
            val planProductBottomFragment = PlanProductBottomFragment(
                null,
                chosenEvent?.ePeople ?: emptyList(),
                chosenEventId.toString()
            )

            planProductBottomFragment.setDismissListener {
                if (it) {
                    viewModel.getPlanProducts(chosenEventId.toString())
                }
            }
            planProductBottomFragment.show(childFragmentManager, "add plan product")
        }

        binding.btnAddPurchase.setOnClickListener {
            val purchaseBottomFragment = PurchaseBottomFragment(
                null, planProducts, chosenEvent?.ePeople ?: emptyList(), chosenEventId.toString()
            )
            purchaseBottomFragment.setDismissListener {
                if (it) {
                    viewModel.getPlanProducts(chosenEventId.toString())
                    viewModel.getPurchases(chosenEventId.toString())
                }
            }
            purchaseBottomFragment.show(childFragmentManager, "add purchase")
        }
        viewModel.getChosenEvent()
        binding.rvPlanProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPlanProducts.adapter = adapter

        binding.rvPurchase.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPurchase.adapter = purchaseAdapter

        observer()
    }

    private fun observer() {
        viewModel.chosenEvent.observe(viewLifecycleOwner) { state ->
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
                    chosenEventId = state.data
                    viewModel.getPlanProducts(chosenEventId.toString())
                    viewModel.getPurchases(chosenEventId.toString())
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
                    list = toSimplePerson(state.data.toMutableList())
//                    list.forEach {
//                        events.forEach { event ->
//                            event.ePlanProducts?.values?.forEach { planProduct ->
//                                planProduct.pWhose?.forEach { simplePerson ->
//                                    if (it.id == simplePerson.id) simplePerson.name = it.name
//                                }
//                            }
//                            event.ePurchases?.values?.forEach { purchase ->
//                                if (purchase.purchaseInfo?.paid?.id == it.id) purchase.purchaseInfo?.paid?.name =
//                                    it.name
//                                purchase.resultProducts?.values?.forEach { resultProduct ->
//                                    resultProduct.rWhose?.forEach {simplePerson ->
//                                        if (it.id == simplePerson.id) simplePerson.name = it.name
//                                    }
//                                }
//                            }
//                            event.ePeople?.forEach {simplePerson ->
//                                if (it.id == simplePerson.id) simplePerson.name = it.name
//                            }
//                        }
//                    }
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
                    events.forEach {
                        updateNames(it, list)
                    }
                    updateUI()
                }
                else -> {
                    binding.progressBar.show()
                }
            }
        }
        viewModel.planProducts.observe(viewLifecycleOwner) { state ->
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
                    planProducts = state.data.toMutableList()
                    adapter.updateList(planProducts)
                }
                else -> {
                    binding.progressBar.show()
                }
            }
        }
        viewModel.purchases.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progressBar.show()
                }
                is UiState.Failure -> {
                    binding.progressBar.hide()
                    Log.e("P", "ОШИБКА")
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.progressBar.hide()
                    purchases = state.data.toMutableList()
                    purchaseAdapter.updateList(purchases)
                }
                else -> {
                    binding.progressBar.show()
                }
            }
        }
    }

    private fun onProductClicked(pos: Int, item: PlanProductModel) {
        val planProductBottomFragment = PlanProductBottomFragment(
            item,
            chosenEvent?.ePeople ?: emptyList(),
            chosenEventId.toString()
        )
        planProductBottomFragment.setDismissListener {
            if (it) {
                viewModel.getPlanProducts(chosenEventId.toString())
            }
        }
        planProductBottomFragment.show(childFragmentManager, "change plan product")
    }

    private fun onPurchaseClicked(pos: Int, item: PurchaseModel) {
        val purchaseBottomFragment = PurchaseBottomFragment(
            item, planProducts, chosenEvent?.ePeople ?: emptyList(), chosenEventId.toString()
        )
        purchaseBottomFragment.setDismissListener {
            if (it) {
                viewModel.getPlanProducts(chosenEventId.toString())
                viewModel.getPurchases(chosenEventId.toString())
            }
        }
        purchaseBottomFragment.show(childFragmentManager, "change purchase")
    }

    private fun toSimplePerson(list: MutableList<PersonModel>): List<SimplePersonModel> {
        val result = mutableListOf<SimplePersonModel>()
        list.forEach {
            result.add(SimplePersonModel(it.id, it.name))
        }
        return result
    }

    private fun updateNames(event: EventModel, list: List<SimplePersonModel>) {
        list.forEach {
            event.ePlanProducts?.values?.forEach { planProduct ->
                planProduct.pWhose?.forEach { simplePerson ->
                    if (it.id == simplePerson.id) simplePerson.name = it.name
                }
                viewModel.updatePlanProduct(event.eInfo?.eKey.toString(), planProduct)
            }
            event.ePurchases?.values?.forEach { purchase ->
                if (purchase.purchaseInfo?.paid?.id == it.id) purchase.purchaseInfo?.paid?.name =
                    it.name
                purchase.resultProducts?.values?.forEach { resultProduct ->
                    resultProduct.rWhose?.forEach { simplePerson ->
                        if (it.id == simplePerson.id) simplePerson.name = it.name
                    }
                }
                viewModel.updatePurchase(event.eInfo?.eKey.toString(), purchase)
            }
            event.ePeople?.forEach { simplePerson ->
                if (it.id == simplePerson.id) simplePerson.name = it.name
            }
        }
        viewModel.updateEvent(event)
    }

    private fun updateUI() {
        if (chosenEventId != null) {
            for (event in events) {
                if (chosenEventId == event.eInfo?.eKey) {
                    chosenEvent = event
                    chosenEvent?.let { updateNames(it, list) }
                    chosenEvent!!.ePlanProducts?.let { adapter.updateList(it.values.toMutableList()) }
                    chosenEvent!!.ePurchases?.values?.toMutableList()
                        ?.let { purchaseAdapter.updateList(it) }
                    binding.tvEventName.text = event.eInfo?.eName
                    binding.tvEventDate.text = event.eInfo?.eDate
                    break
                } else {
                    chosenEvent = null
                    binding.tvEventName.text = "Ничего не выбрано"
                    binding.tvEventDate.text = ""
                }
            }
        } else {
            chosenEvent = null
            binding.tvEventName.text = "Ничего не выбрано"
            binding.tvEventDate.text = ""
        }
    }
}








