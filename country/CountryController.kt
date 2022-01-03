package com.example.scscollision.country

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["countries"])
class CountryController(@Autowired val COUNTRY_SERVICE: CountryService) {

    /****************
     * GET MAPPINGS *
     ****************/

    @GetMapping("/")
    fun getCountries(): List<Country> {
        return COUNTRY_SERVICE.getCountries()
    }

    @GetMapping("/search/")
    fun getSpecificCountries(
        @RequestParam(required = false) id: Long?,
        @RequestParam(required = false) name: String?,
    ): List<Country> {
        return COUNTRY_SERVICE.getSpecificCountries(id, name)
    }

    @GetMapping("/search/incidents/")
    fun getCountriesByNumIncidents(
        @RequestParam() numIncidents: Int,
        @RequestParam() operator: String
    ): List<Country> {
        return COUNTRY_SERVICE.getCountriesByNumIncidents(numIncidents, operator)
    }

    @GetMapping("/search/ships/")
    fun getCountriesByShip(
        @RequestParam(required = false) id: Long?,
        @RequestParam(required = false) shipName: String?,
    ): Country {
        return COUNTRY_SERVICE.getCountryByShip(id, shipName)
    }

    @GetMapping("/search/numberofships/")
    fun getCountriesByNumShips(
        @RequestParam() numShips: Int,
        @RequestParam() operator: String
    ): List<Country> {
        return COUNTRY_SERVICE.getCountryByNumShips(numShips, operator)
    }
}