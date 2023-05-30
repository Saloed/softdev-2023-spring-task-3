package com.example.dacha.ui.people

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import com.example.dacha.R
import com.example.dacha.data.model.NewsModel
import com.example.dacha.data.model.PersonModel
import com.example.dacha.databinding.BottomSheetLayoutBinding
import com.example.dacha.ui.home.HomeViewModel
import com.example.dacha.utils.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime


private const val COLLAPSED_HEIGHT = 260

@AndroidEntryPoint
class PersonBottomFragment(private val person: PersonModel? = null) : BottomSheetDialogFragment() {

    lateinit var binding: BottomSheetLayoutBinding
    val viewModel: PeopleViewModel by viewModels()
    private val homeVM: HomeViewModel by viewModels()
    var closeFunction: ((Boolean) -> Unit)? = null
    var isSuccessAddTask: Boolean = false

    override fun getTheme() = R.style.AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            BottomSheetLayoutBinding.bind(inflater.inflate(R.layout.bottom_sheet_layout, container))
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        val density = requireContext().resources.displayMetrics.density

        dialog?.let {
            val bottomSheet =
                it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            val behavior = BottomSheetBehavior.from(bottomSheet)

            behavior.peekHeight = (COLLAPSED_HEIGHT * density).toInt()
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED

            if (person == null) {
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                binding.layoutCollapsed.visibility = View.GONE
                binding.layoutExpanded.visibility = View.VISIBLE
                binding.bottomText.text = "Ввести данные"
            }

            behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    with(binding) {
                        if (slideOffset > 0) {
                            layoutCollapsed.alpha = 1 - 2 * slideOffset
                            layoutExpanded.alpha = slideOffset * slideOffset

                            if (slideOffset > 0.5) {
                                layoutCollapsed.visibility = View.GONE
                                layoutExpanded.visibility = View.VISIBLE
                            }

                            if (slideOffset < 0.5 && binding.layoutExpanded.visibility == View.VISIBLE) {
                                layoutCollapsed.visibility = View.VISIBLE
                                layoutExpanded.visibility = View.INVISIBLE
                            }
                        }
                    }
                }
            })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        person?.let {
            binding.tvDetailName.text =
                "Имя: ${it.name?.replaceFirstChar { it -> it.uppercaseChar() }}"
            binding.tvDetailBank.text =
                if (!it.bank.isNullOrEmpty()) "Банк: ${it.bank}" else "Банк неизвестен"
            binding.etNamePerson.hint = if (!it.name.isNullOrEmpty()) it.name else "Имя"
            binding.etBankPerson.hint = if (!it.bank.isNullOrEmpty()) it.bank else "Банк"
            binding.etNumberPerson.hint = if (!it.number.isNullOrEmpty()) it.number else "Номер"
            binding.tvDetailNumber.text =
                if (!it.number.isNullOrEmpty()) "Номер: ${it.number}" else "Номер неизвестен"
        }

        if (person == null) {
            binding.etNamePerson.hint = "Имя"
            binding.etBankPerson.hint = "Банк"
            binding.etNumberPerson.hint = "Номер"
        }

        binding.btnDonePerson.setOnClickListener {
            if (person == null && validation()) {
                viewModel.addPerson(getPerson())
            } else if (person != null) {
                viewModel.updatePerson(getPerson())
            }
        }
        observer()
    }

    private fun observer() {
        viewModel.addPerson.observe(viewLifecycleOwner) { state ->
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
                    homeVM.addNews(
                        news(
                            person!!,
                            "Добавил ${state.data.first.name}"
                        )
                    )
                    this.dismiss()
                }
            }
        }
        viewModel.updatePerson.observe(viewLifecycleOwner) { state ->
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
                    homeVM.addNews(
                        news(
                            person!!,
                            "Обновил ${state.data.first.name}"
                        )
                    )
                    this.dismiss()
                }
            }
        }
    }

    private fun validation(): Boolean {
        var isValid = true
        if (binding.etNamePerson.text.isNullOrEmpty()) {
            isValid = false
            toast("Введите имя")
        }
        return isValid
    }

    private fun getPerson(): PersonModel {
        val name = binding.etNamePerson.text.toString().ifEmpty { person?.name }
        val bank = binding.etBankPerson.text.toString().ifEmpty { person?.bank }
        val number = binding.etNumberPerson.text.toString().ifEmpty { person?.number }
        return PersonModel(bank = bank, id = person?.id, name = name, number = number)
    }

    fun setDismissListener(function: ((Boolean) -> Unit)?) {
        closeFunction = function
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        closeFunction?.invoke(isSuccessAddTask)
    }
}