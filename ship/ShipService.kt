package com.example.scscollision.ship

import com.example.scscollision.country.Country
import com.example.scscollision.country.CountryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import mu.KotlinLogging
import java.util.*
import javax.transaction.Transactional
import kotlin.math.abs
import kotlin.math.log
import kotlin.math.min

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
                logger.info { "xcoord: ${ship.xCoord} yCoord: ${ship.yCoord}" }
                SHIP_REPO.save(ship)
                country.addShip(ship)
            }
        }
    }

    /*****************
     * PUT FUNCTIONS *
     *****************/

    fun updateShip(id: Long?,
                   name: String?,
                   newName: String?,
                   newCountryOfOrigin: String?) {

        // Getting the ship to be updated
        val shipList = SHIP_REPO.getShipsByIdOrName(id, name)

        // Checking if error occurred
        if (shipList.isEmpty()) {
            throw Exception("No ship with name: $name and/or id: $id")
        }

        logger.info { "Ship found" }

        val ship = shipList[0]

        // Setting new name
        if (newName != null) {
            logger.info { "Setting new name" }

            if (SHIP_REPO.getShipByName(newName) != null) {
                throw Exception("Another ship already has this name")
            } else {
                logger.info { "new name: $newName added to ship" }
                ship.name = newName
            }
        }

        // Setting new country of origin
        if (newCountryOfOrigin != null) {
            logger.info { "Setting new country of origin" }

            val newCountry = COUNTRY_REPO.getCountryByName(newCountryOfOrigin)
            if (newCountry == null) {
                throw Exception("No Country with this name")
            } else {

                // Remove ship from previous country of origin
                val oldCountry = COUNTRY_REPO.getCountryByName(ship.countryOfOrigin)
                oldCountry?.removeShip(ship)
                if (oldCountry != null) {
                    COUNTRY_REPO.save(oldCountry)
                }
                logger.info { "Removed ship from ${oldCountry?.name}...list looks like ${oldCountry?.ships}" }

                // Adding new country of origin string to ship
                ship.countryOfOrigin = newCountryOfOrigin
                SHIP_REPO.save(ship)
                logger.info { "Switching ship's country of origin" }

                // Add ship to new country of origin
                newCountry.addShip(ship)
                COUNTRY_REPO.save(newCountry)
                logger.info { "Added ship to ${newCountry.name}" }
            }
        }
    }

    @Transactional
    fun updateCoords(id: Long?,
                     name: String?,
                     xCoord: Double,
                     yCoord: Double) {
        val shipList = SHIP_REPO.getShipsByIdOrName(id, name)

        if (shipList.isEmpty()) {
            throw Exception("No ship with name: $name and/or id: $id")
        }

        val ship = shipList[0]

        ship.xCoord = xCoord
        ship.yCoord = yCoord
    }


    /********************
     * DELETE FUNCTIONS *
     ********************/

    fun deleteShip(
        id: Long?,
        name: String?
    ) {
        val shipList = SHIP_REPO.getShipsByIdOrName(id, name)

        if (shipList.isEmpty()) {
            throw Exception("No ship with name: $name and/or id: $id")
        }

        val ship = shipList[0]

        val country = COUNTRY_REPO.getCountryByName(ship.countryOfOrigin)

        if (country != null) {
            country.removeShip(ship)
            COUNTRY_REPO.save(country)
        } else {
            throw Exception("Country not found...fatal error in database")
        }

        SHIP_REPO.delete(ship)
    }

    /******************
     * CLOSEST POINTS *
     ******************/

    fun closestPoints(): Triple<Ship, Ship, Double>? {
        /**
         * Monitors for the closest two ships and determines if there is an
         * incident between them. If so, creates incident report and returns
         * the two ships
         */

        val shipList = SHIP_REPO.findAll()

        // Sort lists by x and y coordinates respectively
        val shipListX = shipList.sortedBy { it.xCoord }
        shipList.sortBy { it.yCoord }

        return closestPointsHelper(shipListX, shipList, shipList.size)
    }

    fun closestPointsHelper(shipsX: List<Ship>, shipsY: List<Ship>, size: Int): Triple<Ship, Ship, Double>? {
        /**
         * Helper function for closest points
         */

        val result: Triple<Ship, Ship, Double>?

        // Brute force if list is small enough
        if (size < 4) {
            result = bruteForce(shipsX)
//            addShip(result!!.first)
//            addShip(result.second)
            if (result == null) {
                logger.info { "NULL" }
            } else {
                logger.info { "ShipA: ${result.first.name} ShipB: ${result.second.name}" }
            }
            return result
        }

        // Find middle point
        val mid = size / 2
        val midpoint = shipsX[mid]

        // Split lists
        val shipsXHalf1 = shipsX.subList(0, mid)
        val shipsXHalf2 = shipsX.subList(mid, size)

        val dr = closestPointsHelper(shipsXHalf1, shipsY, mid)
        val dl = closestPointsHelper(shipsXHalf2, shipsY, size - mid)
        val d: Double
        val curr_pair: Triple<Ship, Ship, Double>

        // Finding the smaller of the two distances
        if (dr!!.third > dl!!.third) {
            d = dl.third
            curr_pair = dl
        } else {
            d = dr.third
            curr_pair = dr
        }

        var stripP = mutableListOf<Ship>()
        var stripQ = mutableListOf<Ship>()

        // Iterate thru list, finding pairs that are closer than d
        for (i in 0..size) {
            if (abs(shipsX[i].xCoord - midpoint.xCoord) < d) {
                stripP.add(shipsX[i])
            }
            if ( abs(shipsY[i].xCoord - midpoint.xCoord) < d) {
                stripQ.add(shipsY[i])
            }
        }

        // Sort stripP by y-coordinate
        stripP.sortBy { ship -> ship.yCoord }

        val minA = stripClosest(stripP, d) ?: curr_pair
        val minB = stripClosest(stripQ, d) ?: curr_pair

        if (minA.third < minB.third) {
            return minA
        } else {
            return minB
        }
    }

    fun bruteForce(shipList: List<Ship>): Triple<Ship, Ship, Double>? {
        /**
         * Brute force closest points algorithm. Returns the pair of closest
         * ships.
         */

        var minVal = 10000000.0
        var closestPair: Triple<Ship, Ship, Double>? = null


        for (i in 0 until (shipList.size - 1)) {
            for (j in 1 until (shipList.size)) {
                logger.info { "INFO: ${shipList[i].getDistBetweenShips(shipList[j])}" }
                if (shipList[i].getDistBetweenShips(shipList[j]) < minVal) {
                    minVal = shipList[i].getDistBetweenShips(shipList[j])
                    closestPair = Triple(shipList[i], shipList[j], minVal)
                }
            }
        }

        return closestPair
    }

    fun stripClosest(shipList: MutableList<Ship>, dist: Double): Triple<Ship, Ship, Double>? {
        /**
         * Calculates the closest two ships and their distance
         * Will only run at max 6 times
         */
        var minVal = dist
        var j = 0
        val size = shipList.size
        var result: Triple<Ship, Ship, Double>? = null

        for (i in 0..size) {
            j = i + 1
            while (j < size && (shipList[j].yCoord - shipList[i].yCoord) < minVal) {
                minVal = shipList[i].getDistBetweenShips(shipList[j])
                result = Triple(shipList[i], shipList[j], minVal)
                j++
            }
        }

        return result
    }
}