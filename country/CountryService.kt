package com.example.scscollision.country

import com.example.scscollision.ship.ShipRepository
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CountryService(@Autowired val COUNTRY_REPO: CountryRepository, val SHIP_REPO: ShipRepository) {

    private val logger = KotlinLogging.logger {}

    /*****************
     * GET FUNCTIONS *
     *****************/

    fun getCountries(): List<Country> {
        logger.info { "getCountries" }
        return COUNTRY_REPO.findAll()
    }

    fun getSpecificCountries(id: Long?, name: String?): List<Country> {
        return COUNTRY_REPO.getSpecificCountries(id, name)
    }

    fun getCountriesByNumIncidents(numIncidents: Int, operator: String): List<Country> {

        return when (operator) {
            "gt" -> COUNTRY_REPO.gtNumIncidents(numIncidents).sortedBy { it.numIncidents }
            "lt" -> COUNTRY_REPO.ltNumIncidents(numIncidents).sortedBy { it.numIncidents }
            "eq" -> COUNTRY_REPO.eqNumIncidents(numIncidents).sortedBy { it.numIncidents }
            else -> {
                throw Exception("please enter a valid operation: 'gt', 'lt', or 'eq'")
            }
        }
    }

    fun getCountryByShip(id: Long?, name: String?): Country {

        val shipList = SHIP_REPO.getShipsByIdOrName(id, name)
        val country: Country?

        if (shipList.isEmpty()) {
            throw Exception("Ship with id $id and name $name does not exist")
        } else {
            country = COUNTRY_REPO.getCountryByName(shipList[0].countryOfOrigin)
                ?: throw Exception(
                    "FATAL ERROR: Country with name ${shipList[0].countryOfOrigin} not found"
                )
        }
        return country
    }

    fun getCountryByNumShips(numShips: Int, operator: String): List<Country> {
        return when (operator) {
            "gt" -> COUNTRY_REPO.gtNumShips(numShips)
            "lt" -> COUNTRY_REPO.ltNumShips(numShips)
            "eq" -> COUNTRY_REPO.eqNumShips(numShips)
            else -> {
                throw Exception("Please enter a valid operation: 'gt', 'lt', or 'eq'")
            }
        }
    }
}