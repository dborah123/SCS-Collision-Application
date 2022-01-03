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

    /*****************
     * POST FUNCTION *
     *****************/

    fun addCountry(name: String) {

        // Check if country name is already take in database
        if (COUNTRY_REPO.getCountryByName(name) != null) {
            throw Exception("Country with $name already taken")
        } else {
            COUNTRY_REPO.save(Country(name = name))
        }
    }

    /*******************
     * UPDATE FUNCTION *
     *******************/

    fun updateCountryName(id: Long?, name: String?, newName: String) {

        // Getting country
        if (id == null && name == null) {
            throw Exception("Id and name not specified")
        }

        val countryList = COUNTRY_REPO.getCountryByIdOrName(id, name)

        // Checking if country query is correct
        if (countryList.isEmpty()) {
            throw Exception("Country with name $name and id #$id not found")
        } else if (countryList.size != 1) {
            throw Exception("FATAL ERROR: More than one country found")
        }

        // Making sure there isn't a country with newName already
        if (COUNTRY_REPO.getCountryByName(newName) != null) {
            throw Exception(
                "Another country already has name $newName"
            )
        }

        val country = countryList[0]
        country.name = newName
        COUNTRY_REPO.save(country)

        // Updating the ships' country of origin
        for (ship in country.ships) {
            ship.countryOfOrigin = newName
            SHIP_REPO.save(ship)
        }
    }

    /*******************
     * DELETE FUNCTION *
     *******************/

    fun deleteCountry(id: Long?, name: String?) {
        if (id == null && name == null) {
            throw Exception("Id and name not specified")
        }

        val country = COUNTRY_REPO.getCountryByIdOrName(id, name)

        if (country.isEmpty()) {
            throw Exception("Country with name $name and id #$id not found")
        } else if (country.size != 1) {
            throw Exception("FATAL ERROR: More than one country found")
        }

        COUNTRY_REPO.delete(country[0])
    }
}