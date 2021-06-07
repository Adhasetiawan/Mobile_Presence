package com.example.mobilepresence.model.response

object LocationObject {
    data class LocationResponse(
        val Success: String,
        val location: List<Location>
    )

    data class Location(
        val id_location: Int,
        val latitude: Double,
        val longitude: Double
    )
}