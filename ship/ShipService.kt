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
import javax.transaction.Transactional
import kotlin.math.log

@Service
class ShipService(@Autowired val SHIP_REPO: ShipRepository, val COUNTRY_REPO: CountryRepository) {

    private val logger = KotlinLogging.logger {}

    /*****************
     * GET FUNCTIONS *
     *****************/

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
            distFromCenter = ship.getDistFromCoords(x_coord, y_coord)

            if (distFromCenter > radius) {
                logger.info { "Ship id #${ship.id} removed" }
                iterator.remove()
            }
        }

        return ships
    }

    fun getDistBetween(
        shipAId: Long,
        shipBId: Long
    ): Double {
        val ships = SHIP_REPO.findAllById(mutableListOf(shipAId, shipBId))

        return ships[0].getDistBetweenShips(ships[1])
    }

    fun getNearestShip(
        shipId: Long?,
        name: String?
    ): Ship? {

        var shipAOptional: Optional<Ship>

        // Make sure at least one param is not null
        if (shipId == null && name == null) {
            logger.error { "Both shipId and name not specified" }
            throw Exception("Need to specify shipId or name")
        }

        // Get shipA
        if (shipId != null) {
            shipAOptional = SHIP_REPO.findById(shipId)

            if (shipAOptional.isEmpty && name == null) {
                logger.error { "Ship with id '$shipId' not found" }
                throw Exception("Ship with id '$shipId' not found")
            } else if (name != null) {
                shipAOptional = SHIP_REPO.getShipByNameOptional(name)


            }
        } else if (name != null) {
            shipAOptional = SHIP_REPO.getShipByNameOptional(name)
        }
        else {
            shipAOptional = Optional.empty()
        }

        if (shipAOptional.isEmpty) {
            logger.error { "Ship with id '$shipId' and name '$name' not found" }
            throw Exception("Ship with id '$shipId' and name '$name' not found")
        }

        // Get shipA
        val shipA = shipAOptional.get()

        // Query for all ships and set up initial variables
        val allShips = SHIP_REPO.findAll()
        if (allShips.size < 2) {
            logger.error { "Fewer than two ships" }
            throw Exception("Fewer than two ships")
        }

        var closestShip: Ship? = null
        var dist: Double = 10000000000.0

        // Iterate thru ships to find closest ship
        for (shipB in allShips) {
            if (shipB.id != shipA.id
                && shipA.getDistBetweenShips(shipB) < dist) {
                closestShip = shipB
                dist = shipA.getDistBetweenShips(shipB)
            }
        }

        return closestShip
    }

    /******************
     * POST FUNCTIONS *
     ******************/

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
                throw Exception(
                    "Country ${ship.countryOfOrigin} does not exist in database"
                )
            } else {
                SHIP_REPO.save(ship)
                country.addShip(ship)
            }
        }
    }

    /*****************
     * PUT FUNCTIONS *
     *****************/

    @Transactional
    fun updateShip(id: Long?,
                   name: String?,
                   newName: String?,
                   newCountryOfOrigin: String?) {

        // Getting the ship to be updated
        val shipList = SHIP_REPO.getShipsByIdOrName(id, name)

        // Checking if error occurred
        if (shipList.size > 1) {
            logger.error { "Name not specific enough" }
            throw Exception("Name not specific enough")
        } else if (shipList.isEmpty()) {
            logger.error { "No ship with name: $name and id: $id" }
            throw Exception("No ship with name: $name and id: $id")
        }

        val ship = shipList[0]

        // Setting new name
        if (newName != null) {
            if (SHIP_REPO.getShipByName(newName) != null) {
                logger.error { "Another ship already has this name" }
                throw Exception("Another ship already has this name")
            } else {
                ship.name = newName
            }
        }

        // Setting new country of origin
        if (newCountryOfOrigin != null) {
            val newCountry = COUNTRY_REPO.getCountryByName(newCountryOfOrigin)
            if (newCountry == null) {
                logger.error { "Another ship already has this name" }
                throw Exception("Another ship already has this name")
            } else {
                // Remove ship from previous country of origin
                val oldCountry = COUNTRY_REPO.getCountryByName(ship.countryOfOrigin)
                oldCountry?.removeShip(ship)

                // Add ship to new country of origin
                newCountry.addShip(ship)
            }
        }
    }
}