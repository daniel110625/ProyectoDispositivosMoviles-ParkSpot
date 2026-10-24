package com.example.myappparkspot.Util

object Util {

    fun isValidText(value: String): Boolean {
        return value.trim().isNotEmpty()
    }

    fun isValidPrice(price: Double): Boolean {
        return price >= 0
    }

    fun isValidCoordinates(latitude: Double, longitude: Double): Boolean {
        return latitude in -90.0..90.0 &&
                longitude in -180.0..180.0
    }
}