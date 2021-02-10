package com.example.myapplication.views.create

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.example.myapplication.BR
import com.example.myapplication.R
import com.example.myapplication.viewmodels.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.user_create_dialog_fragment.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class UserCreateDialogFragment : BottomSheetDialogFragment(), AdapterView.OnItemSelectedListener,
    View.OnFocusChangeListener {

    private val viewModel: MainViewModel by sharedViewModel<MainViewModel>()
    lateinit var binding: ViewDataBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<ViewDataBinding>(
            inflater,
            R.layout.user_create_dialog_fragment, container, false
        )
        binding.lifecycleOwner = this

        binding.setVariable(BR.viewModel, viewModel)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        genderInput.onItemSelectedListener = this
        genderInput.onFocusChangeListener = this
        viewModel.dissmiss.observe(viewLifecycleOwner, Observer {
            this.dismiss()

        })

        statutsInput.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                viewModel.newUserStatus.postValue("Active")
            } else {
                viewModel.newUserStatus.postValue("Inactive")
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val genderArr = resources.getStringArray(R.array.gender_spinner)
        viewModel.newUserGender.postValue(genderArr[position])
        viewModel.newUserGenderInt.postValue(position)
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (hasFocus) hideKeyboard()
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }


}