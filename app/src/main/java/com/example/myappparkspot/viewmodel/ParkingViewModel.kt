package com.example.myappparkspot.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myappparkspot.domain.model.Parking
import com.example.myappparkspot.domain.repository.IParkingRepository

class ParkingViewModel(private val repository: IParkingRepository) :
    ViewModel() {

    fun getParkings(): List<Parking> {
        return repository.getAll()
    }

    fun getParkingById(id: Int): Parking? {
        return repository.getById(id)
    }

    fun createParking(parking: Parking) {
        try {
            if (repository.getById(parking.id) == null)
                repository.add(parking)
            else
                throw Exception(
                    "The parking is duplicated. " +
                            "Please add a new one."
                )
        } catch (e: Exception) {
            throw e
        }
    }

    fun updateParking(parking: Parking) {
        try {
            if (repository.getById(parking.id) != null)
                repository.update(parking)
            else
                throw Exception(
                    "It's not possible to update the parking " +
                            "because it does not exist."
                )
        } catch (e: Exception) {
            throw e
        }
    }

    fun removeParking(id: Int) {
        try {
            if (repository.getById(id) != null)
                repository.delete(id)
            else
                throw Exception(
                    "It's not possible to delete the parking " +
                            "because it does not exist."
                )
        } catch (e: Exception) {
            throw e
        }
    }
}