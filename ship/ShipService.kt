package com.example.scscollision.ship

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.math.pow
import kotlin.math.sqrt
import mu.KotlinLogging

@Service
class ShipService(@Autowired val SHIP_REPO: ShipRepository) {

    private val logger = KotlinLogging.logger {}

    fun getShips(): List<Ship> {
        logger.info { "getShips" }
        return SHIP_REPO.findAll()
    }

    fun getSpecificShips(
        id: Long?,
        name: String?,
        countryOfOrigin: String?
    ): List<Ship> {
        logger.info { "getSpecificShips" }
        return SHIP_REPO.getSpecificShips(id, name, countryOfOrigin)
    }

    fun getShipsInRadius(
        x_coord: Double,
        y_coord: Double,
        radius: Double
    ): MutableList<Ship> {
        val ships = SHIP_REPO.findAll()
        val iterator = ships.iterator()
        var distFromCenter: Double

        logger.info { "getShipInRadius" }

        while (iterator.hasNext()) {
            val ship = iterator.next()
            // Get distance using pythagorean theorem
            distFromCenter = sqrt(((ship.x_coords - x_coord).pow(2))
                                    + ((ship.y_coords - y_coord).pow(2))
                                )
            if (distFromCenter > radius) {
                logger.info { "Ship id #${ship.id} removed" }
                iterator.remove()
            }
        }

        return ships
    }

    fun getDistBetween(shipAId: Long, shipBId: Long): Double {
        val ships = SHIP_REPO.findAllById(mutableListOf(shipAId, shipBId))

        return sqrt(((ships[0].x_coords - ships[1].x_coords).pow(2))
                + ((ships[0].y_coords - ships[1].y_coords).pow(2)))
    }
}