package com.example.scscollision.ship

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.math.pow
import kotlin.math.sqrt

@Service
class ShipService(@Autowired val SHIP_REPO: ShipRepository) {

    fun getShips(): List<Ship> {
        return SHIP_REPO.findAll()
    }

    fun getSpecificShips(
        id: Long?,
        name: String?,
        countryOfOrigin: String?
    ): List<Ship> {
        return SHIP_REPO.getSpecificShips(id, name, countryOfOrigin)
    }

    fun getShipsInRadius(
        x_coord: Double,
        y_coord: Double,
        radius: Double
    ): MutableList<Ship> {
        val ships = SHIP_REPO.findAll()
        var distFromCenter: Double

        for (ship in ships) {
            // Get distance using pythagorean theorem
            distFromCenter = sqrt(((ship.x_coords - x_coord).pow(2))
                                    + ((ship.y_coords - y_coord).pow(2))
                                )
            if (distFromCenter > radius) {
                println("deleted")
                ships.remove(ship)
            }
        }

        return ships
    }
}