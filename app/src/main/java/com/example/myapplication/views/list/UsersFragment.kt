package com.example.myapplication.views.list

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.myapplication.BR
import com.example.myapplication.R
import com.example.myapplication.model.User
import com.example.myapplication.viewmodels.MainViewModel
import com.example.myapplication.common.networkStatus.NetworkStatusInterface
import org.koin.android.viewmodel.ext.android.sharedViewModel


class UsersFragment : Fragment(){

    private val viewModel: MainViewModel by sharedViewModel<MainViewModel>()
    lateinit var binding: ViewDataBinding
    var networkStatusInterface: NetworkStatusInterface? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<ViewDataBinding>(
            inflater,
            R.layout.users_fragment, container, false
        )
        binding.lifecycleOwner = this

        binding.setVariable(BR.viewModel, viewModel)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val nav =  Navigation.findNavController(requireActivity(), R.id.fragment_main_container_view)

        viewModel.navigate.observe(viewLifecycleOwner, Observer {dest->
            dest.let {
                nav.navigate(it)
            }
        })

        viewModel.deleteUser.observe(viewLifecycleOwner, Observer {user->
            showSureDeleteAlert(user)
        })

        viewModel.networkStatus.observe(viewLifecycleOwner, Observer {ns->
            networkStatusInterface?.setNetWorkStatus(ns)
        })
    }

    private fun showSureDeleteAlert(user: User?) {
        val alertDialog: AlertDialog = AlertDialog.Builder(requireActivity()).create()
        alertDialog.setTitle("Delete")
        alertDialog.setMessage("Are you sure you want to delete "+ user?.name)
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
            DialogInterface.OnClickListener { dialog, which ->
                user?.let { viewModel.sendDeleteUser(it) }
                dialog.dismiss()
            })
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
            DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
        alertDialog.show()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            networkStatusInterface = context as NetworkStatusInterface
        } catch (castException: ClassCastException) {
            /** The activity does not implement the listener.  */
        }
    }

    override fun onDetach() {
        super.onDetach()
        try {
            networkStatusInterface = null
        } catch (castException: ClassCastException) {
            /** The activity does not implement the listener.  */
        }
    }
}