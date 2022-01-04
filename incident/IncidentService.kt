package com.example.scscollision.incident

import com.example.scscollision.country.Country
import com.example.scscollision.country.CountryRepository
import com.example.scscollision.ship.Ship
import com.example.scscollision.ship.ShipRepository
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class IncidentService(@Autowired
                      val INCIDENT_REPO: IncidentRepository,
                      val SHIP_REPO: ShipRepository,
                      val COUNTRY_REPO: CountryRepository) {

    private val logger = KotlinLogging.logger {}

    /*****************
     * GET FUNCTIONS *
     *****************/

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    fun getIncidents(id: Long?,
                     shipAName: String?,
                     shipAId: Long?,
                     shipBName: String?,
                     shipBId: Long?,
    ): List<Incident> {

        // Getting ship id's if necessary
        val shipA: Ship?
        val shipB: Ship?
        var aId: Long? = null
        var bId: Long? = null

        if (shipAId != null) {
            aId = shipAId
            logger.info { "In correct case aId: $aId" }
        } else if (shipAName != null) {
            shipA = SHIP_REPO.getShipByName(shipAName)

            if (shipA == null) {
                throw Exception("Ship with name $shipAName and/or id $shipAId does not exist")
            } else {
                aId = shipA.id ?: throw Exception("FATAL ERROR: Ship ${shipA.name} has id of null")
            }
        }

        if (shipBId != null) {
            bId = shipBId
        } else if (shipBName != null) {
            shipB = SHIP_REPO.getShipByName(shipBName)

            if (shipB == null) {
                throw Exception("Ship with name $shipAName and/or id $shipAId does not exist")
            } else {
                bId = shipB.id ?: throw Exception("FATAL ERROR: Ship ${shipB.name} has id of null")
            }
        }
        logger.info { "id: $id, aId: $aId, bId: $bId" }
        return INCIDENT_REPO.getIncidents(id, aId, bId)
    }

    fun getIncidentsByCountry(
        countryAId: Long?,
        countryAName: String?,
        countryBId: Long?,
        countryBName: String?
    ): List<Incident> {

        var aId: Long? = null
        var bId: Long? = null

        // Checking if only one country is specified
        if (countryAId == null && countryAName == null) {
            val countryList = COUNTRY_REPO.getCountryByIdOrName(countryBId, countryBName)

            if (countryList.isEmpty()) {
                throw Exception(
                    "Country of name $countryAName and/or id $countryAId does not exist"
                )
            } else if (countryList.size != 1) {
                throw Exception(
                    "FATAL ERROR: More than one country returned"
                )
            }

            return countryList[0].incidents
        } else if (countryAId != null) {
            aId = countryAId
        } else {
            if (countryAName != null) {
                val country = COUNTRY_REPO.getCountryByName(countryAName)
                    ?: throw Exception("Country with name $countryAName is not found")
                aId = country.id
            }
        }

        if (countryBId == null && countryBName == null) {
            val countryList = COUNTRY_REPO.getCountryByIdOrName(countryAId, countryAName)

            if (countryList.isEmpty()) {
                throw Exception(
                    "Country of name $countryBName and/or id $countryBId does not exist"
                )
            } else if (countryList.size != 1) {
                throw Exception(
                    "FATAL ERROR: More than one country returned"
                )
            }

            return countryList[0].incidents
        } else if (countryBId != null) {
            bId = countryBId
        } else {
            if (countryBName != null) {
                val country = COUNTRY_REPO.getCountryByName(countryBName)
                    ?: throw Exception("Country with name $countryBName is not found")
                bId = country.id
            }
        }

        return INCIDENT_REPO.getIncidentsByCountries(aId, bId)
    }
}