package com.example.myapplication.common.networkStatus

sealed class NetworkStatus {
    data class Loading(var loading: Boolean) : NetworkStatus()
    data class Error(var errorMsg: String) : NetworkStatus()
    data class Success(var successMessage: String) : NetworkStatus()

}