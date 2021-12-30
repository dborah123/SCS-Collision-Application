package com.example.scscollision.country

import com.example.scscollision.ship.ShipRepository
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CountryService(@Autowired val COUNTRY_REPO: CountryRepository) {

    private val logger = KotlinLogging.logger {}

    fun getCountries(): List<Country> {
        logger.info { "getCountries" }
        return COUNTRY_REPO.findAll()
    }

    fun getSpecificCountries(id: Long?, name: String?): List<Country> {
        return COUNTRY_REPO.getSpecificBooks(id, name)
    }
}