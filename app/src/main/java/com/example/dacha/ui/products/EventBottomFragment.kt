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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.util.forEach
import androidx.fragment.app.viewModels
import com.example.dacha.R
import com.example.dacha.data.model.EventInfo
import com.example.dacha.data.model.EventModel
import com.example.dacha.data.model.SimplePersonModel
import com.example.dacha.databinding.EventChangeBottomSheetBinding
import com.example.dacha.utils.UiState
import com.example.dacha.utils.hide
import com.example.dacha.utils.show
import com.example.dacha.utils.toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import java.time.YearMonth

@AndroidEntryPoint
class EventBottomFragment(private val event: EventModel?, private val toDelete: Boolean, val people: List<SimplePersonModel>) : BottomSheetDialogFragment() {

    lateinit var binding: EventChangeBottomSheetBinding
    val viewModel: ProductsViewModel by viewModels()
    var closeFunction: ((Boolean) -> Unit)? = null
    var isSuccessAddTask: Boolean = false


//    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    private var date: String = "00.00.2000"

    var day = "00"
    var month = "00"
    var year = "2000"



    override fun getTheme() = R.style.AppBottomSheetDialogTheme


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            EventChangeBottomSheetBinding.bind(inflater.inflate(R.layout.event_change_bottom_sheet, container))
        val tvTop: TextView = binding.tvEventTop
        val tfName: TextInputLayout = binding.tvNameEvent
        val eventDatePicker: ConstraintLayout = binding.eventDatePicker

        val dayPicker = binding.dayFilledExposed
        val monthPicker = binding.monthFilledExposed
        val yearPicker = binding.yearFilledExposed
        val lvPeoplePicker: ListView = binding.eventPeoplePicker
        val btnDone = binding.eventChangeBtn

        val listOfPeople = mutableListOf<String>()

        val days = mutableListOf<Int>()
        for (i in 1..31) days.add(i)
        val months = mutableMapOf<Int, String>()
        months[0] = "Январь"
        months[1] = "Февраль"
        months[2] = "Март"
        months[3] = "Апрель"
        months[4] = "Май"
        months[5] = "Июнь"
        months[6] = "Июль"
        months[7] = "Август"
        months[8] = "Сентябрь"
        months[9] = "Октябрь"
        months[10] = "Ноябрь"
        months[11] = "Декабрь"
        val years = mutableListOf<Int>()
        for (i in 2023..2043) years.add(i)


        if (toDelete) {
            tvTop.text = "Удалить поездку?"
            tfName.visibility = View.GONE
            eventDatePicker.visibility = View.GONE
            lvPeoplePicker.visibility = View.GONE
            binding.tvEventPeople.visibility = View.GONE
            binding.eventAllPeopleBtn.visibility = View.GONE
            btnDone.text = "Удалить"
        }
        else {
            people.forEach {
                listOfPeople.add(it.name.toString())
            }
            val lvAdapter = ArrayAdapter(this.requireContext(), android.R.layout.simple_list_item_multiple_choice, listOfPeople)
            lvPeoplePicker.adapter = lvAdapter
            if (event == null) {
                binding.etNameEvent.hint = "Поездка"
                binding.dayFilledExposed.hint = "День"
                binding.monthFilledExposed.hint = "Месяц"
                binding.yearFilledExposed.hint = "Год"
                tvTop.text = "Добавить"
            }
            else {

                date = event.eInfo?.eDate.toString()
                day = event.eInfo?.eDate.toString().split(".")[0]
                month = event.eInfo?.eDate.toString().split(".")[1]
                year = event.eInfo?.eDate.toString().split(".")[2]
                binding.dayFilledExposed.hint = day
                binding.monthFilledExposed.hint = months[month.toInt() - 1]
                binding.yearFilledExposed.hint = year
                binding.etNameEvent.hint = event.eInfo?.eName.toString()
                tvTop.text = "Изменить данные"
                event.ePeople?.forEach {
                if (it.name in listOfPeople) lvPeoplePicker.setItemChecked(listOfPeople.indexOf(it.name), true)
                else lvPeoplePicker.setItemChecked(listOfPeople.indexOf(it.name), false)
                }

            }
        }









        val daysAdapter = ArrayAdapter(this.requireContext(), R.layout.drop_down_item, days)
        dayPicker.setAdapter(daysAdapter)

        val monthsAdapter = ArrayAdapter(this.requireContext(), R.layout.drop_down_item, months.values.toTypedArray())
        monthPicker.setAdapter(monthsAdapter)

        val yearAdapter = ArrayAdapter(this.requireContext(), R.layout.drop_down_item, years)
        yearPicker.setAdapter(yearAdapter)

        dayPicker.setOnItemClickListener { _, _, i, _ ->
            day = String.format("%02d", days[i])
        }

        monthPicker.setOnItemClickListener { _, _, i, _ ->
            month = String.format("%02d", i + 1)
        }
        yearPicker.setOnItemClickListener { _, _, i, _ ->
            year = years[i].toString()
        }





        binding.eventAllPeopleBtn.setOnClickListener {
            if (lvPeoplePicker.checkedItemCount != listOfPeople.size) {
                for (i in listOfPeople.indices) {
                    lvPeoplePicker.setItemChecked(i, true)
                }
            }
            else {
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
            if (event == null && validation(day, month, year)) viewModel.addEvent(getEvent())
            else {
                if (toDelete) {
                    if (event != null) {
                        viewModel.deleteEvent(event)
                    }
                }
                else {
                    if (validation(day, month, year)) viewModel.updateEvent(getEvent())
                }
            }
            observer()
        }


    }

    private fun observer(){
        viewModel.addEvent.observe(viewLifecycleOwner) {state ->
            when(state){
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
                    binding.progressBar.hide()
                    toast(state.data.second)
                    this.dismiss()
                }
            }
        }
        viewModel.updateEvent.observe(viewLifecycleOwner) {state ->
            when(state){
                is UiState.Loading -> {
                    binding.progressBar.show()
                }
                is UiState.Failure -> {
                    binding.progressBar.hide()
                    toast(state.error)
                }
                is UiState.Success -> {
                    isSuccessAddTask = true
                    Log.e("EU", state.data.first.toString())
                    viewModel.chooseEvent(state.data.first)
                    binding.progressBar.hide()
                    toast(state.data.second)
                    this.dismiss()
                }
            }
        }
        viewModel.deleteEvent.observe(viewLifecycleOwner) {state ->
            when(state){
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

    private fun getEvent(): EventModel {
        val name = binding.etNameEvent.text.toString().ifEmpty { event?.eInfo?.eName }
        val thisDate: String = if (event?.eInfo?.eDate == null) {
            "$day.$month.$year"
        } else {
            if (day != "00" || month != "00" || year != "2000"){
                "$day.$month.$year"
            } else {
                event.eInfo?.eDate.toString()
            }
        }

        val chosenPeople = mutableListOf<SimplePersonModel>()
        binding.eventPeoplePicker.checkedItemPositions.forEach { key, value ->
            if (value) {
                chosenPeople.add(people[key])
            }
        }

        Log.e("EBF", event?.ePlanProducts.toString())
        return EventModel(EventInfo(eDate = thisDate, eKey = event?.eInfo?.eKey, eName = name) , ePeople = chosenPeople, event?.ePlanProducts, event?.ePurchases)
    }

    fun setDismissListener(function: ((Boolean) -> Unit)?) {
        closeFunction = function
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        closeFunction?.invoke(isSuccessAddTask)
    }

    private fun validation(day: String, month: String, year: String): Boolean {
        val yearMonth = YearMonth.of(year.toInt(), month.toInt())
        return day.toInt() <= yearMonth.lengthOfMonth()
    }
}