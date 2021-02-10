package com.example.myapplication.di

import com.example.myapplication.networking.RestApiService
import com.example.myapplication.networking.ServiceBuilder
import com.example.myapplication.viewmodels.MainViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {

    single { RestApiService() }
    single { ServiceBuilder }

    viewModel {
        MainViewModel(
            RestApiService()
        )
    }
}