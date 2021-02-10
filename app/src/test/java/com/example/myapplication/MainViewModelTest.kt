package com.example.myapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myapplication.networking.RestApiService
import com.example.myapplication.viewmodels.MainViewModel
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class MainViewModelTest {

    private lateinit var myViewModel: MainViewModel

    @Rule @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var apiService: RestApiService

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        myViewModel = MainViewModel(apiService)
    }

    @Test
    fun isNotValidEmpty(){
        assertEquals(false, myViewModel.isValid())
    }

    @Test
    fun isNotValidEmail(){
        myViewModel.newUserName.value = "name"
        myViewModel.newUserEmail.value = "emailWRONG"
        myViewModel.newUserStatus.value = "Active"
        myViewModel.newUserGender.value = "Female"
        assertEquals(false, myViewModel.isValid())
    }

    @Test
    fun isNotValidEmptyName(){
        myViewModel.newUserName.value = ""
        myViewModel.newUserEmail.value = "email@email.com"
        myViewModel.newUserStatus.value = "Active"
        myViewModel.newUserGender.value = "Female"
        assertEquals(false, myViewModel.isValid())
    }

    @Test
    fun isNotValidEmptyGender(){
        myViewModel.newUserName.value = "name"
        myViewModel.newUserEmail.value = "email@email.com"
        myViewModel.newUserStatus.value = "Active"
        myViewModel.newUserGenderInt.value = 0
        assertEquals(false, myViewModel.isValid())
    }

    @Test
    fun isValid(){
        myViewModel.newUserName.value = "name"
        myViewModel.newUserEmail.value = "email@email.com"
        myViewModel.newUserStatus.value = "Active"
        myViewModel.newUserGenderInt.value = 1
        assertEquals(true, myViewModel.isValid())
    }

}