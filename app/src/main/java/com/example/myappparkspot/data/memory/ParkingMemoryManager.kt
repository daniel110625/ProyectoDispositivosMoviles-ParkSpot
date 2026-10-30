package com.example.myappparkspot.data.memory

import com.example.myappparkspot.domain.model.Parking
import com.example.myappparkspot.domain.repository.IParkingRepository

class ParkingMemoryManager : IParkingRepository {

    private val parkingList = mutableListOf<Parking>()

    override fun getAll(): List<Parking> {
        return parkingList
    }

    override fun getById(id: Int): Parking? {
        return parkingList.find { it.id == id }
    }

    override fun add(parking: Parking) {
        parkingList.add(parking)
    }

    override fun update(parking: Parking) {
        val index = parkingList.indexOfFirst { it.id == parking.id }

        if (index != -1) {
            parkingList[index] = parking
        }
    }

    override fun delete(id: Int) {
        parkingList.removeAll { it.id == id }
    }
}