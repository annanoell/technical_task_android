package com.example.myapplication.viewmodels

import android.text.TextUtils
import android.view.View
import androidx.core.util.PatternsCompat
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.R
import com.example.myapplication.common.SingleLiveData
import com.example.myapplication.model.User
import com.example.myapplication.model.UserResponse
import com.example.myapplication.networking.RestApiService
import com.example.myapplication.views.list.UsersRecyclerViewAdapter
import com.example.myapplication.common.networkStatus.NetworkStatus
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(private val apiService: RestApiService) : ViewModel() {

    val navigate: SingleLiveData<Int> = SingleLiveData()
    var newUser: User? = null
    val newUserName: MutableLiveData<String> = MutableLiveData()
    val newUserNameError: MutableLiveData<String> = MutableLiveData()
    val newUserEmail: MutableLiveData<String> = MutableLiveData()
    val newUserEmailError: MutableLiveData<String> = MutableLiveData()
    val newUserGenderInt: MutableLiveData<Int> = MutableLiveData(0)
    val newUserGender: MutableLiveData<String> = MutableLiveData()
    val newUserGenderError: MutableLiveData<String> = MutableLiveData()
    val newUserStatus: MutableLiveData<String> = MutableLiveData("Inactive")
    val dissmiss: SingleLiveData<Boolean> = SingleLiveData()
    val deleteUser: SingleLiveData<User> = SingleLiveData()


    val networkStatus = SingleLiveData<NetworkStatus>()

    private var lastUsersPage = 1

    init {

        getUsers()
    }

    val adapter: UsersRecyclerViewAdapter =
        UsersRecyclerViewAdapter(
            this,
            mutableListOf()
        )


    val dataIsValid = MediatorLiveData<Boolean>().apply {
        addSource(newUserName) {
            value = isValid()
        }
        addSource(newUserEmail) {
            value = isValid()
        }

        addSource(newUserGenderInt)
        {
            value = isValid()
        }
    }

    fun isValid(): Boolean? {

        newUserGenderError.postValue("")
        newUserNameError.postValue("")
        newUserEmailError.postValue("")

        if (newUserGenderInt.value == 0) {
            return false
        }
        if (newUserName.value.isNullOrEmpty()) {
            return false
        }
        if (newUserEmail.value != null && TextUtils.isEmpty(newUserEmail.value) ||
            !PatternsCompat.EMAIL_ADDRESS.matcher(
                newUserEmail.value
            ).matches()
        ) {
            newUserEmailError.postValue("Invalid Email address")
            return false
        }

        return true
    }

    fun userCreatedAt(position: Int): String {
        var format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
        val newDate: Date = format.parse(adapter.users[position].created_at) ?: return ""
        format = SimpleDateFormat("dd MMM,yyyy hh:mm a", Locale.getDefault())
        return format.format(newDate)
    }

    private fun getUsers(page: Int = 1) {
        networkStatus.postValue(NetworkStatus.Loading(true))
        apiService.getUsers(page) { success, result ->
            networkStatus.postValue(NetworkStatus.Loading(false))
            if (success) {
                result?.let { checkIfPageIsValid(it) }
            } else {
                //Show Error
                networkStatus.postValue(NetworkStatus.Error("An error occurred"))
            }
        }
    }

    private fun checkIfPageIsValid(result: UserResponse) {
        if (result.meta.pagination.pages != result.meta.pagination.page) {
            lastUsersPage = result.meta.pagination.pages
            getUsers(result.meta.pagination.pages)
        } else {
            adapter.addItems(result.data)
        }
    }

    fun deleteUser(user: User) {
        deleteUser.postValue(user)
    }

    fun sendDeleteUser(user: User) {
        networkStatus.postValue(NetworkStatus.Loading(true))
        apiService.deleteUser(user.id) { success ->
            networkStatus.postValue(NetworkStatus.Loading(false))

            if (success) {
                adapter.removeUser(user)
                networkStatus.postValue(NetworkStatus.Success("User removed"))

            } else {
                //Show Error
                networkStatus.postValue(NetworkStatus.Error("An error occurred"))

            }
        }
    }

    fun onAddUserClick(v: View) {
        navigate.postValue(R.id.action_usersFragment_to_userCreateDialogFragment)
    }

    fun saveUser(v: View) {
        newUser = User()
        newUser!!.name = newUserName.value.toString()
        newUser!!.email = newUserEmail.value.toString()
        newUser!!.gender = newUserGender.value.toString()
        newUser!!.status = newUserStatus.value.toString()

        if (newUserGenderInt.value == 0) {
            newUserGenderError.postValue("You must choose one option")
            return
        }
        if (newUserName.value.isNullOrEmpty()) {
            newUserNameError.postValue("You must provide a name")
            return
        }


        networkStatus.postValue(NetworkStatus.Loading(true))
        apiService.postUser(newUser!!) { success, user ->
            networkStatus.postValue(NetworkStatus.Loading(false))
            if (success) {
                resetInput()
                adapter.addItem(user)
                dissmiss.postValue(true)
                networkStatus.postValue(NetworkStatus.Success("User saved"))

            } else {
                //Show Error
                networkStatus.postValue(NetworkStatus.Error("An error occurred"))
            }
        }
    }

    private fun resetInput() {
        newUser = User()
        newUserEmail.postValue("")
        newUserGender.postValue("")
        newUserName.postValue("")
        newUserStatus.postValue("")
    }
}