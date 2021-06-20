package com.example.mobilepresence.model.response

object RecordObject {
    data class ObjectResponse(
        val Success: String,
        val page_number: String,
        val total_page: Int,
        val trackrecord: List<Trackrecord>
    )

    data class Trackrecord(
        val date: String,
        val id_post: Int,
        val location: String
    )
}