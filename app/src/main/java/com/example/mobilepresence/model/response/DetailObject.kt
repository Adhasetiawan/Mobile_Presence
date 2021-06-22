package com.example.mobilepresence.model.response

object DetailObject {
    data class DetailResponse(
        val Success: String,
        val detail: Detail
    )

    data class Detail(
        val arrivetime: String,
        val date: String,
        val id_post: Int,
        val id_user: Int,
        val latitude: Double,
        val leavingtime: String,
        val location: String,
        val longitude: Double,
        val post: String
    )
}