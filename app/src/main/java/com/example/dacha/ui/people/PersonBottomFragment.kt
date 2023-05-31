package com.example.dacha.ui.people

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import com.example.dacha.R
import com.example.dacha.data.model.PersonModel
import com.example.dacha.databinding.BottomSheetLayoutBinding
import com.example.dacha.ui.home.HomeViewModel
import com.example.dacha.utils.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint


private const val COLLAPSED_HEIGHT = 260

@AndroidEntryPoint
class PersonBottomFragment(private val updatingPerson: PersonModel? = null) :
    BottomSheetDialogFragment() {

    lateinit var binding: BottomSheetLayoutBinding
    val viewModel: PeopleViewModel by viewModels()
    private val homeVM: HomeViewModel by viewModels()
    var closeFunction: ((Boolean) -> Unit)? = null
    var isSuccessAddTask: Boolean = false
    var person = PersonModel()

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

            if (updatingPerson == null) {
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                binding.layoutCollapsed.visibility = View.GONE
                binding.layoutExpanded.visibility = View.VISIBLE
                binding.bottomText.text = getString(R.string.input_data)
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

        updatingPerson?.let {
            binding.tvDetailName.text = getString(
                R.string.name,
                it.name?.replaceFirstChar { letter -> letter.uppercaseChar() })
            binding.tvDetailBank.text =
                if (!it.bank.isNullOrEmpty()) getString(
                    R.string.bank,
                    it.bank
                ) else getString(R.string.bank, getString(R.string.no_info))
            binding.etNamePerson.hint = it.name
            binding.etBankPerson.hint =
                if (it.bank.isNullOrEmpty()) getString(R.string.simple_bank) else it.bank
            binding.etNumberPerson.hint =
                if (it.number.isNullOrEmpty()) getString(R.string.simple_number) else it.number
            binding.tvDetailNumber.text =
                if (!it.number.isNullOrEmpty()) getString(
                    R.string.number,
                    it.number
                ) else getString(R.string.number, getString(R.string.no_info))
        }

        if (updatingPerson == null) {
            binding.etNamePerson.hint = getString(R.string.simple_name)
            binding.etBankPerson.hint = getString(R.string.simple_bank)
            binding.etNumberPerson.hint = getString(R.string.simple_number)
        }

        binding.btnDonePerson.setOnClickListener {
            if (updatingPerson == null && validation()) {
                viewModel.addPerson(getUpdatingPerson())
            } else if (updatingPerson != null) {
                viewModel.updatePerson(getUpdatingPerson())
            }
        }
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
                    homeVM.addNews(
                        news(
                            person,
                            getString(R.string.add, state.data.name)
                        )
                    )
                    toast(getString(R.string.person) + " " + getString(R.string.added))
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
                    homeVM.addNews(
                        news(
                            person, getString(R.string.update, state.data.name)
                        )
                    )
                    toast(getString(R.string.person) + " " + getString(R.string.updated))
                    this.dismiss()
                }
            }
        }
    }

    private fun validation(): Boolean {
        var isValid = true
        if (binding.etNamePerson.text.isNullOrEmpty()) {
            isValid = false
            toast(getString(R.string.empty_field))
        }
        return isValid
    }

    private fun getUpdatingPerson(): PersonModel {
        val name = binding.etNamePerson.text.toString().ifEmpty { updatingPerson?.name }
        val bank = binding.etBankPerson.text.toString().ifEmpty { updatingPerson?.bank }
        val number = binding.etNumberPerson.text.toString().ifEmpty { updatingPerson?.number }
        return PersonModel(bank = bank, id = updatingPerson?.id, name = name, number = number)
    }

    fun setDismissListener(function: ((Boolean) -> Unit)?) {
        closeFunction = function
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        closeFunction?.invoke(isSuccessAddTask)
    }
}