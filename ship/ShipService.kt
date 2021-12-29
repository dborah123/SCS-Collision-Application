package com.example.scscollision.ship

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ShipService(@Autowired val SHIP_REPO: ShipRepository) {

    fun getShips(): List<Ship> {
        return SHIP_REPO.findAll()
    }
}