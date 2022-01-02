package com.example.scscollision.country

import com.example.scscollision.ship.ShipRepository
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CountryService(@Autowired val COUNTRY_REPO: CountryRepository) {

    private val logger = KotlinLogging.logger {}

    /*****************
     * GET FUNCTIONS *
     *****************/

    fun getCountries(): List<Country> {
        logger.info { "getCountries" }
        return COUNTRY_REPO.findAll()
    }

    fun getSpecificCountries(id: Long?, name: String?): List<Country> {
        return COUNTRY_REPO.getSpecificBooks(id, name)
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

    fun getCountriesByShips(id: Long?, name: String?, numShips: Int?, operator: String?): List<Country> {
        return listOf()
    }
}