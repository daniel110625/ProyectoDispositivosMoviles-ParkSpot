package com.example.myappparkspot.domain.repository

import com.example.myappparkspot.domain.model.Parking

interface IParkingRepository {

    fun getAll(): List<Parking>

    fun getById(id: Int): Parking?

    fun add(parking: Parking)

    fun update(parking: Parking)

    fun delete(id: Int)
}