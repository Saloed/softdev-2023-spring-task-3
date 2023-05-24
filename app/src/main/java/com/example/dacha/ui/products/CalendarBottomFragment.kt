package com.example.dacha.ui.products

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dacha.R
import com.example.dacha.data.model.EventModel
import com.example.dacha.databinding.CalendarDayBinding
import com.example.dacha.databinding.CalendarHeaderBinding
import com.example.dacha.databinding.EventCalendarBottomSheetLayoutBinding
import com.example.dacha.databinding.EventItemViewBinding
import com.example.dacha.utils.UiState
import com.example.dacha.utils.hide
import com.example.dacha.utils.show
import com.example.dacha.utils.toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import dagger.hilt.android.AndroidEntryPoint
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
class EventsAdapter(var chosenEventSent: EventModel?, val onClick: (EventModel, View) -> Unit) :
    RecyclerView.Adapter<EventsAdapter.EventsViewHolder>() {

    val events = mutableListOf<EventModel>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        return EventsViewHolder(
            EventItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        )

    }

    override fun onBindViewHolder(viewHolder: EventsViewHolder, position: Int) {
        viewHolder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size

    inner class EventsViewHolder(private val binding: EventItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onClick(events[bindingAdapterPosition], it)
            }
        }

        fun bind(event: EventModel) {
            if (event == chosenEventSent) {
                itemView.setBackgroundColor(Color.parseColor("#E8E6D3"))
            }
            else itemView.setBackgroundColor(Color.WHITE)
            binding.itemEventText.text = event.eInfo?.eName
        }
    }
}

@AndroidEntryPoint
class CalendarBottomFragment(private val rawEvents: List<EventModel>, private val oldEvent: EventModel?) : BottomSheetDialogFragment() {



    private var selectedDate: LocalDate? = null
    private var chosenEvent = oldEvent
    private var chosenView: View? = null
    private val today = LocalDate.now()

    private val titleSameYearFormatter = DateTimeFormatter.ofPattern("MMMM")
    private val titleFormatter = DateTimeFormatter.ofPattern("MMM yyyy")
    private val selectionFormatter = DateTimeFormatter.ofPattern("d MMM yyyy")

    val viewModel: ProductsViewModel by viewModels()
    var closeFunction: ((Boolean) -> Unit)? = null
    var isSuccessAddTask: Boolean = false


    private val events = mutableMapOf<LocalDate, MutableList<EventModel>>()

    lateinit var binding: EventCalendarBottomSheetLayoutBinding
    override fun getTheme() = R.style.AppBottomSheetDialogTheme

    private val eventsAdapter = EventsAdapter(chosenEvent) { event, view ->

        if (chosenEvent == event) {
            chosenView?.setBackgroundColor(resources.getColor(R.color.white))
            chosenEvent = oldEvent
        }
        else {
            chosenEvent = event
            view.setBackgroundColor(resources.getColor(R.color.second_text_color))
            chosenView = view
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EventCalendarBottomSheetLayoutBinding.bind(inflater.inflate(R.layout.event_calendar_bottom_sheet_layout, container))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        rawEvents.forEach {
            val date = LocalDate.parse(it.eInfo?.eDate, formatter)
            if (date in events.keys) {
                events[date]?.add(it)
            }
            else {
                events[date] = mutableListOf(it)
            }
        }

        binding = EventCalendarBottomSheetLayoutBinding.bind(view)

        binding.rvCalendar.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = eventsAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
        }

        binding.calendarView.monthScrollListener = {
            binding.tvCalendarInfo.text = if (it.yearMonth.year == today.year) {
                titleSameYearFormatter.format(it.yearMonth)
            } else {
                titleFormatter.format(it.yearMonth)
            }
            selectDate(it.yearMonth.atDay(1))
        }

        val daysOfWeek = daysOfWeek(firstDayOfWeek = DayOfWeek.MONDAY)
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(50)
        val endMonth = currentMonth.plusMonths(50)

        configureBinders(daysOfWeek)
        binding.calendarView.apply {
            setup(startMonth, endMonth, daysOfWeek.first())
            scrollToMonth(currentMonth)
        }

        if (savedInstanceState == null) {
            binding.calendarView.post { selectDate(today) }
        }

        binding.eventChooseBtn.setOnClickListener {
            if (chosenEvent != null) {
                viewModel.chooseEvent(chosenEvent!!)
            }
            observer()
        }
    }

    private fun observer(){
        viewModel.chooseEvent.observe(viewLifecycleOwner) {state ->
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

    fun setDismissListener(function: ((Boolean) -> Unit)?) {
        closeFunction = function
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        closeFunction?.invoke(isSuccessAddTask)
    }

    private fun selectDate(date: LocalDate) {
        if (selectedDate != date) {
            val oldDate = selectedDate
            selectedDate = date
            oldDate?.let { binding.calendarView.notifyDateChanged(it) }
            binding.calendarView.notifyDateChanged(date)
            updateAdapterForDate(date)
        }
    }

    private fun updateAdapterForDate(date: LocalDate) {
        eventsAdapter.apply {
            events.clear()
            this.chosenEventSent = chosenEvent
            events.addAll(this@CalendarBottomFragment.events[date].orEmpty())
            notifyDataSetChanged()
        }
        binding.selectedDateText.text = selectionFormatter.format(date)
    }

    private fun configureBinders(daysOfWeek: List<DayOfWeek>) {
        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay
            val binding = CalendarDayBinding.bind(view)

            init {
                view.setOnClickListener {
                    if (day.position == DayPosition.MonthDate) {
                        selectDate(day.date)
                    }
                }
            }
        }
        binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.day = data
                val textView = container.binding.dayText
                val dotView = container.binding.dotView

                textView.text = data.date.dayOfMonth.toString()

                if (data.position == DayPosition.MonthDate) {
                    textView.show()
                    when (data.date) {
                        today -> {
                            textView.setTextColor(resources.getColor(R.color.white))
                            textView.setBackgroundResource(R.drawable.today_dark_bg)
                            dotView.hide()
                        }
                        selectedDate -> {
                            textView.setTextColor(resources.getColor(R.color.new_colorSecondary))
                            textView.setBackgroundResource(R.drawable.selected_dark_bg)
                            dotView.hide()
                        }
                        else -> {
                            textView.setTextColor(resources.getColor(R.color.black))
                            textView.background = null
                            dotView.isVisible = events[data.date].orEmpty().isNotEmpty()
                        }
                    }
                } else {
                    textView.hide()
                    dotView.hide()
                }
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val legendLayout = CalendarHeaderBinding.bind(view).legendLayout.root
        }
        binding.calendarView.monthHeaderBinder =
            object : MonthHeaderFooterBinder<MonthViewContainer> {
                override fun create(view: View) = MonthViewContainer(view)
                override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                    if (container.legendLayout.tag == null) {
                        container.legendLayout.tag = data.yearMonth
                        container.legendLayout.children.map { it as TextView }
                            .forEachIndexed { index, tv ->
                                tv.text = daysOfWeek[index].name.first().toString()
                                tv.setTextColor(resources.getColor(R.color.black))
                            }
                    }
                }
            }
    }
}


