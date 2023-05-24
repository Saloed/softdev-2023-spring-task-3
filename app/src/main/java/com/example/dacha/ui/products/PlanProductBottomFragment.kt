package com.example.dacha.ui.products

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import androidx.core.util.forEach
import androidx.fragment.app.viewModels
import com.example.dacha.R
import com.example.dacha.data.model.PersonModel
import com.example.dacha.data.model.PlanProductModel
import com.example.dacha.data.model.SimplePersonModel
import com.example.dacha.databinding.BottomSheetLayoutBinding
import com.example.dacha.databinding.PlanProductBottomSheetLayoutBinding
import com.example.dacha.ui.people.PeopleViewModel
import com.example.dacha.utils.UiState
import com.example.dacha.utils.hide
import com.example.dacha.utils.show
import com.example.dacha.utils.toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PlanProductBottomFragment(
    val product: PlanProductModel?,
    val people: List<SimplePersonModel>,
    val eventId: String
) : BottomSheetDialogFragment() {

    lateinit var binding: PlanProductBottomSheetLayoutBinding
    val viewModel: ProductsViewModel by viewModels()
    var closeFunction: ((Boolean) -> Unit)? = null
    var isSuccessAddTask: Boolean = false

    override fun getTheme() = R.style.AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            PlanProductBottomSheetLayoutBinding.bind(
                inflater.inflate(
                    R.layout.plan_product_bottom_sheet_layout,
                    container
                )
            )

        val tvTop = binding.tvPlanProductTop
        val btnDelete = binding.btnDeletePlanProduct
        val tfName = binding.etPlanProductName
        val tfAmount = binding.etPlanProductAmount
        val btnAddAll = binding.planProductAllPeopleBtn
        val lvPeople = binding.planProductPeoplePicker

        val listOfPeople = mutableListOf<String>()
        people.forEach {
            listOfPeople.add(it.name.toString())
        }

        val lvAdapter = ArrayAdapter(
            this.requireContext(),
            android.R.layout.simple_list_item_multiple_choice,
            listOfPeople
        )
        lvPeople.adapter = lvAdapter



        if (product == null) {
            btnDelete.hide()
            tvTop.text = "Добавить"
            tfName.hint = "Название"
            tfAmount.hint = "Количество"
        } else {
            btnDelete.show()
            tvTop.text = "Изменить"
            tfName.hint = product.pProduct
            tfAmount.hint = product.pAmount.toString()
            product.pWhose?.forEach {
                if (it.name in listOfPeople) lvPeople.setItemChecked(
                    listOfPeople.indexOf(it.name),
                    true
                )
                else lvPeople.setItemChecked(listOfPeople.indexOf(it.name), false)
            }
        }

        btnAddAll.setOnClickListener {
            if (lvPeople.checkedItemCount != listOfPeople.size) {
                for (i in listOfPeople.indices) {
                    lvPeople.setItemChecked(i, true)
                }
            } else {
                for (i in listOfPeople.indices) {
                    lvPeople.setItemChecked(i, false)
                }
            }

        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ppDoneBtn.setOnClickListener {
            if (product == null && validation()) {
                viewModel.addPlanProduct(eventId, getPlanProduct())
            } else if (product != null) {
                viewModel.updatePlanProduct(eventId, getPlanProduct())
            }
        }
        binding.btnDeletePlanProduct.setOnClickListener {
            if (product != null) {
                viewModel.deletePlanProduct(eventId, getPlanProduct())
            }
        }
        observer()
    }

    private fun observer() {
        viewModel.addPlanProduct.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progressBar.show()
                }
                is UiState.Failure -> {
                    binding.progressBar.hide()
                    toast(state.error)
                }
                is UiState.Success -> {
                    isSuccessAddTask = true
                    binding.progressBar.hide()
                    toast(state.data.second)
                    this.dismiss()
                }
            }
        }
        viewModel.updatePlanProduct.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progressBar.show()
                }
                is UiState.Failure -> {
                    binding.progressBar.hide()
                    toast(state.error)
                }
                is UiState.Success -> {
                    isSuccessAddTask = true
                    binding.progressBar.hide()
                    toast(state.data.second)
                    this.dismiss()
                }
            }
        }
        viewModel.deletePlanProduct.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progressBar.show()
                }
                is UiState.Failure -> {
                    binding.progressBar.hide()
                    toast(state.error)
                }
                is UiState.Success -> {
                    isSuccessAddTask = true
                    binding.progressBar.hide()
                    toast(state.data.second)
                    this.dismiss()
                }
            }
        }
    }

    private fun validation(): Boolean {
        var isValid = true
        if (binding.etPlanProductName.text.isNullOrEmpty()) {
            isValid = false
            toast("Введите название")
        }
        return isValid
    }

    private fun getPlanProduct(): PlanProductModel {
        val name = binding.etPlanProductName.text.toString().ifEmpty { product?.pProduct }
        val amount: Double
        val strAmount = binding.etPlanProductAmount.text.toString()
        strAmount.replace(',', '.')

        amount = if (strAmount != "") strAmount.toDouble()
        else product?.pAmount!!
        val chosenPeople = mutableListOf<SimplePersonModel>()
        binding.planProductPeoplePicker.checkedItemPositions.forEach { key, value ->
            if (value) {
                chosenPeople.add(people[key])
            }
        }
        return PlanProductModel(pProduct = name, pAmount = amount, pKey = product?.pKey, pWhose = chosenPeople)
    }

    fun setDismissListener(function: ((Boolean) -> Unit)?) {
        closeFunction = function
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        closeFunction?.invoke(isSuccessAddTask)
    }
}