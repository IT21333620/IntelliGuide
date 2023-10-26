package com.example.intelliguide.data

data class LocationInfo(
    val locationName: String = "",
    val timestamp: Long = 0,
    val placeURL: String = ""
) {
    // Default constructor required for Firebase
    constructor() : this("", 0, "")
}

