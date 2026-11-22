
package com.example.myappparkspot.data.memory

import com.example.myappparkspot.domain.repository.IParkingRepository

object ParkingRepositoryProvider {

    val repository: IParkingRepository = ParkingMemoryManager()

}
