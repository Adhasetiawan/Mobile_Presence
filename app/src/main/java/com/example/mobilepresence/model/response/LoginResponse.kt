package com.example.mobilepresence.model.response

object LoginResponse {

    data class LoginResponse(
        val data_user: DataUser,
        val massage: String,
        val success: Boolean,
        val token: String
)

    data class DataUser(
        val id_user: Int,
        val username: String,
        val name: String,
        val division: String,
        val address: String,
        val role: String,
        val picture: String
    )
}