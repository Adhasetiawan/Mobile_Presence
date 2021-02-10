package com.example.mobilepresence.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mobilepresence.model.repository.LoginRepository

class ProfileViewModel (private val loginRepository: LoginRepository) : ViewModel() {

    fun getPict() = loginRepository.getPict()
    fun getName() = loginRepository.getName()
    fun getDivision() = loginRepository.getDivision()
    fun getIdUser() = loginRepository.getId()
    fun getRole() = loginRepository.getRole()
}