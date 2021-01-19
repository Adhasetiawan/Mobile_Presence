package com.example.mobilepresence.model

sealed class UiState<T> {
    data class Loading<T>(val isLoading: Boolean) : UiState<T>()
    data class Success<T>(val data : T) : UiState<T>()
    data class Error<T>(val throwable: Throwable) : UiState<T>()
}