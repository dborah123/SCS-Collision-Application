package com.example.scscollision.ship

import com.example.scscollision.country.Country
import com.example.scscollision.country.CountryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.math.pow
import kotlin.math.sqrt
import mu.KotlinLogging
import org.springframework.web.bind.annotation.PostMapping
import java.util.*

@Service
class ShipService(@Autowired val SHIP_REPO: ShipRepository, val COUNTRY_REPO: CountryRepository) {

    private val logger = KotlinLogging.logger {}

    // GET FUNCTIONS:
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

    // POST FUNCTIONS:

    fun addShip(ship: Ship) {
        logger.info { "addShip Service Layer" }
        val shipOptional: Ship? = SHIP_REPO.getShipByName(ship.name)

        // Check if ship already exists in database
        if (shipOptional != null) {
            logger.error { "Ship already in database" }
            throw Exception("Ship already exists in database")
        } else {
            val country: Country? = COUNTRY_REPO.getCountryByName(ship.countryOfOrigin)

            // Check if country exists in database
            if (country == null) {
                logger.error { "Country does not exist in database" }
                throw Exception("Country ${ship.countryOfOrigin} does not exist in database")
            } else {
                SHIP_REPO.save(ship)
                country.addShip(ship)
            }
        }
    }

}