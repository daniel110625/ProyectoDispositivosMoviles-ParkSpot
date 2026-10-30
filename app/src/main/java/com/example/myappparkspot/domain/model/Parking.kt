package com.example.myappparkspot.domain.model

data class Parking(
    val id: Int,
    var name: String,
    var address: String,
    var pricePerHour: Double,
    var vehicleType: VehicleType,
    var parkingType: ParkingType,
    var openingHours: String,
    var imageUri: String?,
    var latitude: Double,
    var longitude: Double
)