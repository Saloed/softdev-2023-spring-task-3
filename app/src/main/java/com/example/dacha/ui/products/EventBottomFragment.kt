package com.example.dacha.ui.products

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.core.util.forEach
import androidx.fragment.app.viewModels
import com.example.dacha.R
import com.example.dacha.data.model.*
import com.example.dacha.databinding.EventChangeBottomSheetBinding
import com.example.dacha.ui.home.HomeViewModel
import com.example.dacha.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class EventBottomFragment(
    private val event: EventModel?,
    private val toDelete: Boolean,
    val people: List<SimplePersonModel>,
    val person: PersonModel
) : BottomSheetDialogFragment() {

    lateinit var binding: EventChangeBottomSheetBinding
    val viewModel: ProductsViewModel by viewModels()
    private val homeVM: HomeViewModel by viewModels()
    var closeFunction: ((Boolean) -> Unit)? = null
    var isSuccessAddTask: Boolean = false

    private var date: String? = event?.eInfo?.eDate

//    var day = "00"
//    var month = "00"
//    var year = "2000"


    override fun getTheme() = R.style.AppBottomSheetDialogTheme


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            EventChangeBottomSheetBinding.bind(
                inflater.inflate(
                    R.layout.event_change_bottom_sheet,
                    container
                )
            )

        val tvTop: TextView = binding.tvEventTop
        val tfName: TextInputLayout = binding.tvNameEvent
        val datePicker = binding.datePicker
        val lvPeoplePicker: ListView = binding.eventPeoplePicker
        val btnDone = binding.eventChangeBtn
        val listOfPeople = mutableListOf<String>()




        if (toDelete) {
            tvTop.text = "Удалить поездку?"
            tfName.hide()
            datePicker.hide()
            lvPeoplePicker.hide()
            binding.tvEventPeople.hide()
            binding.eventAllPeopleBtn.hide()
            btnDone.text = "Удалить"
        } else {
            people.forEach {
                listOfPeople.add(it.name.toString())
            }
            val lvAdapter = ArrayAdapter(
                this.requireContext(),
                android.R.layout.simple_list_item_multiple_choice,
                listOfPeople
            )
            lvPeoplePicker.adapter = lvAdapter
            val today = Calendar.getInstance()
            if (event == null) {
                datePicker.init(
                    today.get(Calendar.YEAR), today.get(Calendar.MONTH),
                    today.get(Calendar.DAY_OF_MONTH)
                ) { _, year, month, day ->
                    date = "${String.format("%02d", day)}.${String.format("%02d", month + 1)}.$year"
                }
                tvTop.text = "Добавить"
            } else {

                val dateSplit = event.eInfo?.eDate.toString().split(".")
                val oldDay = dateSplit[0]
                val oldMonth = dateSplit[1]
                val oldYear = dateSplit[2]
                datePicker.init(
                    oldYear.toInt(),
                    oldMonth.toInt() - 1,
                    oldDay.toInt()
                ) { _, year, month, day ->
                    date = "${String.format("%02d", day)}.${String.format("%02d", month + 1)}.$year"
                }
                binding.etNameEvent.hint = event.eInfo?.eName.toString()
                tvTop.text = "Изменить данные"
                event.ePeople?.forEach {
                    if (it.name in listOfPeople) lvPeoplePicker.setItemChecked(
                        listOfPeople.indexOf(
                            it.name
                        ), true
                    )
                    else lvPeoplePicker.setItemChecked(listOfPeople.indexOf(it.name), false)
                }
            }
        }

        binding.eventAllPeopleBtn.setOnClickListener {
            if (lvPeoplePicker.checkedItemCount != listOfPeople.size) {
                for (i in listOfPeople.indices) {
                    lvPeoplePicker.setItemChecked(i, true)
                }
            } else {
                for (i in listOfPeople.indices) {
                    lvPeoplePicker.setItemChecked(i, false)
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.eventChangeBtn.setOnClickListener {
            if (event == null) viewModel.addEvent(getEvent())
            else {
                if (toDelete) {
                    viewModel.deleteEvent(event)
                } else {
                    viewModel.updateEvent(getEvent())
                }
            }
            observer()
        }
    }

    private fun observer() {
        viewModel.addEvent.observe(viewLifecycleOwner) { state ->
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
                    viewModel.chooseEvent(state.data.first)
                    homeVM.addNews(
                        news(
                            person,
                            "Добавил(а) поездку: ${state.data.first.eInfo?.eName}"
                        )
                    )
                    binding.progressBar.hide()
                    toast(state.data.second)
                    this.dismiss()
                }
            }
        }
        viewModel.updateEvent.observe(viewLifecycleOwner) { state ->
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
                    viewModel.chooseEvent(state.data.first)
                    homeVM.addNews(
                        news(
                            person,
                            "Обновил(а) поездку: ${state.data.first.eInfo?.eName}"
                        )
                    )
                    binding.progressBar.hide()
                    toast(state.data.second)
                    this.dismiss()
                }
            }
        }
        viewModel.deleteEvent.observe(viewLifecycleOwner) { state ->
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
                    homeVM.addNews(
                        news(
                            person,
                            "Удалил(а) поездку: ${state.data.first.eInfo?.eName}"
                        )
                    )
                    toast(state.data.second)
                    this.dismiss()
                }
            }
        }
    }

    private fun getEvent(): EventModel {
        val name = binding.etNameEvent.text.toString().ifEmpty { event?.eInfo?.eName }
        val thisDate = date
        val chosenPeople = mutableListOf<SimplePersonModel>()
        binding.eventPeoplePicker.checkedItemPositions.forEach { key, value ->
            if (value) {
                chosenPeople.add(people[key])
            }
        }
        return EventModel(
            EventInfo(eDate = thisDate, eKey = event?.eInfo?.eKey, eName = name),
            ePeople = chosenPeople,
            event?.ePlanProducts,
            event?.ePurchases
        )
    }

    fun setDismissListener(function: ((Boolean) -> Unit)?) {
        closeFunction = function
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        closeFunction?.invoke(isSuccessAddTask)
    }
}