package com.example.mobilepresence.model.response

object RecordObject {
    data class TrackrecordResponse(
    val Status: String,
    val Total: Int,
    val Trackrecord: List<Trackrecord>
)

data class Trackrecord(
    val date: String,
    val id_post: Int,
    val location: String
)
}