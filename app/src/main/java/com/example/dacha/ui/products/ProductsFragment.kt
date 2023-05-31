package com.example.dacha.ui.products


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dacha.R
import com.example.dacha.data.model.*
import com.example.dacha.databinding.FragmentProductsBinding
import com.example.dacha.ui.home.HomeViewModel
import com.example.dacha.utils.UiState
import com.example.dacha.utils.hide
import com.example.dacha.utils.show
import com.example.dacha.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsFragment : Fragment() {

    val viewModel: ProductsViewModel by viewModels()
    private val homeVM: HomeViewModel by viewModels()
    lateinit var binding: FragmentProductsBinding
    val adapter by lazy {
        PlanProductAdapter(onProductClicked = { item -> onProductClicked(item) })
    }

    private val purchaseAdapter by lazy {
        PurchaseAdapter(onPurchaseClicked = { item -> onPurchaseClicked(item) })
    }

    var person = PersonModel()

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
        homeVM.getPeople()
        viewModel.getEvents()
        homeVM.getPerson()
        binding = FragmentProductsBinding.inflate(inflater, container, false)
        binding.productsToolbar.inflateMenu(R.menu.event_menu)
        binding.productsToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.addEvent -> {
                    val eventBottomFragment = EventBottomFragment(null, false, list, person)
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
                        chosenEvent, false, list, person
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
                    val eventBottomFragment = EventBottomFragment(chosenEvent, true, list, person)
                    eventBottomFragment.setDismissListener {
                        if (it) {
                            viewModel.getEvents()
                            adapter.updateList(emptyList<PlanProductModel>().toMutableList())
                            purchaseAdapter.updateList(emptyList<PurchaseModel>().toMutableList())
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

        binding.tbgChooseProducts.addOnButtonCheckedListener { _, checkedId, isChecked ->
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

        if (chosenEventId.isNullOrEmpty()) {
            binding.btnAddPlanProduct.setOnClickListener {
                val planProductBottomFragment = PlanProductBottomFragment(
                    null,
                    chosenEvent?.ePeople ?: emptyList(),
                    chosenEventId.toString(),
                    person
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
                    null,
                    planProducts,
                    chosenEvent?.ePeople ?: emptyList(),
                    chosenEvent as EventModel,
                    person
                )
                purchaseBottomFragment.setDismissListener {
                    if (it) {
                        viewModel.getPlanProducts(chosenEventId.toString())
                        viewModel.getPurchases(chosenEventId.toString())
                    }
                }
                purchaseBottomFragment.show(childFragmentManager, "add purchase")
            }
        }
        viewModel.getChosenEvent()
        binding.rvPlanProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPlanProducts.adapter = adapter

        binding.rvPurchase.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPurchase.adapter = purchaseAdapter

        observer()
    }

    private fun observer() {
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
                    if (state.data == null) toast(getString(R.string.go_login))
                    else person = state.data
                }
                else -> {
                    binding.progressBar.show()
                }
            }
        }
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
        homeVM.people.observe(viewLifecycleOwner) { state ->
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

    private fun onProductClicked(item: PlanProductModel) {
        val planProductBottomFragment = PlanProductBottomFragment(
            item,
            chosenEvent?.ePeople ?: emptyList(),
            chosenEventId.toString(),
            person
        )
        planProductBottomFragment.setDismissListener {
            if (it) {
                viewModel.getPlanProducts(chosenEventId.toString())
            }
        }
        planProductBottomFragment.show(childFragmentManager, "change plan product")
    }

    private fun onPurchaseClicked(item: PurchaseModel) {
        val purchaseBottomFragment = PurchaseBottomFragment(
            item,
            planProducts,
            chosenEvent?.ePeople ?: emptyList(),
            chosenEvent as EventModel,
            person
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
                    binding.tvEventName.text = getString(R.string.nothing_chosen)
                    binding.tvEventDate.text = ""
                }
            }
        } else {
            chosenEvent = null
            binding.tvEventName.text = getString(R.string.nothing_chosen)
            binding.tvEventDate.text = ""
        }
    }
}








