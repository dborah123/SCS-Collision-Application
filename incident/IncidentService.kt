package com.example.scscollision.incident

import com.example.scscollision.country.Country
import com.example.scscollision.country.CountryRepository
import com.example.scscollision.ship.Ship
import com.example.scscollision.ship.ShipRepository
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
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

    fun getIncidentsByRadius(
        xCoord: Double,
        yCoord: Double,
        radius: Double
    ): MutableList<Incident> {
        /**
         * Returns list of incidents that occurred in a radius
         */
        val incidentsList = INCIDENT_REPO.findAll()
        val iterator = incidentsList.iterator()
        var distFromCoords: Double
        var incident: Incident

        // Remove elements outisde of radius
        while (iterator.hasNext()) {
            incident = iterator.next()
            distFromCoords = incident.getDistFromCoords(xCoord, yCoord)

            if (distFromCoords > radius) {
                iterator.remove()
            }
        }

        return incidentsList
    }

    /******************
     * POST FUNCTIONS *
     ******************/

    fun addIncident(
        shipAId: Long?,
        shipAName: String?,
        shipBId: Long?,
        shipBName: String?,
        datetimeString: String?,
        now: Boolean?
    ) {
        /**
         * Adds a new incident
         */
        val datetime: LocalDateTime

        // Checking if datetime or now is provided
        if (datetimeString == null && (now == null || !now)) {
            throw Exception("Datetime or now (boolean) not specified")
        } else if (datetimeString != null && now == true) {
            throw Exception("Cannot specify both datetime $datetimeString and now (boolean) true")
        }

        // Getting shipA
        val shipAList = SHIP_REPO.getShipsByIdOrName(shipAId, shipAName)

        if (shipAList.isEmpty()) {
            throw Exception("shipA with name $shipAName and/or id $shipAId not found")
        } else if (shipAList.size > 1) {
            throw Exception("Multiple ships found with name $shipAName and id $shipAId")
        }
        val shipA = shipAList[0]

        // Getting second country involved
        val countryA = COUNTRY_REPO.getCountryByName(shipA.countryOfOrigin)
            ?: throw Exception("Country with name ${shipA.countryOfOrigin}")

        // Getting shipB
        val shipBList = SHIP_REPO.getShipsByIdOrName(shipBId, shipBName)

        if (shipBList.isEmpty()) {
            throw Exception("shipB with name $shipBName and/or id $shipBId not found")
        } else if (shipBList.size > 1) {
            throw Exception("Multiple ships found with name $shipBName and id $shipBId")
        }
        val shipB = shipBList[0]

        // Getting second country involved
        val countryB = COUNTRY_REPO.getCountryByName(shipB.countryOfOrigin)
            ?: throw Exception("Country with name ${shipB.countryOfOrigin} not found")

        // Getting datetime
        if (now != null && now) {
            datetime = LocalDateTime.now()
        } else {
            // Parse datetime from string
            val datetimeFormatter = DateTimeFormatter.ofPattern(
                "uuuu-mm-dd hh:mm a",
                Locale.ENGLISH
            )

            datetime = LocalDateTime.parse(datetimeString, datetimeFormatter)
        }

        // Create and save incident
        val incident = Incident(
            shipA = shipA,
            shipB = shipB,
            time = datetime,
            countriesInvolved = mutableSetOf(countryA, countryB)
        )

        INCIDENT_REPO.save(incident)

        // Add incident to countries and save
        countryA.addIncident(incident)
        countryB.addIncident(incident)
        COUNTRY_REPO.saveAll(listOf(countryA, countryB))
    }

    /*****************
     * PUT FUNCTIONS *
     *****************/

    fun updateIncident(
        id: Long,
        shipAId: Long?,
        shipAName: String?,
        shipBId: Long?,
        shipBName: String?,
        datetimeString: String?,
        xCoord: Double?,
        yCoord: Double?
    ) {

        // Getting incident
        val incidentOptional = INCIDENT_REPO.findById(id)
        val incident: Incident

        if (incidentOptional.isEmpty) {
            throw Exception("Error: incident with id $id not found")
        }

        incident = incidentOptional.get()

        // Setting new shipA
        if (shipAId != null || shipAName != null) {
            // Getting new ship
            val shipA = SHIP_REPO.getShipsByIdOrName(shipAId, shipAName)

            if (shipA.isEmpty()) {
                throw Exception("Ship with id $shipAId and/or name $shipAName was not found")
            }

            // Removing old country from countries involved
            val iterator = incident.countriesInvolved.iterator()
            var countryIter: Country

            while (iterator.hasNext()) {
                countryIter = iterator.next()

                if (countryIter.name == incident.shipA.countryOfOrigin) {
                    // Remove country from incident
                    incident.countriesInvolved.remove(countryIter)
                    INCIDENT_REPO.save(incident)

                    // Remove incident from country
                    countryIter.removeIncident(incident)
                    COUNTRY_REPO.save(countryIter)
                }
            }

            // Adding new Ship to incident
            incident.shipA = shipA[0]
            val countryString = shipA[0].countryOfOrigin
            countryIter = COUNTRY_REPO.getCountryByName(countryString)!!

            // Saving country
            countryIter.addIncident(incident)
            COUNTRY_REPO.save(countryIter)

            // Saving incident
            incident.countriesInvolved.add(countryIter)
            INCIDENT_REPO.save(incident)
        }

        // Setting new shipB
        if (shipBId != null || shipBName != null) {
            // Getting new ship
            val shipB = SHIP_REPO.getShipsByIdOrName(shipBId, shipBName)

            if (shipB.isEmpty()) {
                throw Exception("Ship with id $shipBId and/or name $shipBName was not found")
            }

            // Removing old country from countries involved
            val iterator = incident.countriesInvolved.iterator()
            var countryIter: Country
            while (iterator.hasNext()) {
                countryIter = iterator.next()

                if (countryIter.name == incident.shipB.countryOfOrigin) {
                    // Remove country from incident
                    incident.countriesInvolved.remove(countryIter)
                    INCIDENT_REPO.save(incident)

                    // Remove incident from country
                    countryIter.removeIncident(incident)
                    COUNTRY_REPO.save(countryIter)
                }
            }

            // Adding new Ship to incident
            incident.shipB = shipB[0]
            val countryString = shipB[0].countryOfOrigin
            countryIter = COUNTRY_REPO.getCountryByName(countryString)!!

            // Saving country
            countryIter.addIncident(incident)
            COUNTRY_REPO.save(countryIter)

            // Saving incident
            incident.countriesInvolved.add(countryIter)
            INCIDENT_REPO.save(incident)
        }

        // Changing datetime
        if (datetimeString != null) {
            // Parse datetime from string
            val datetimeFormatter = DateTimeFormatter.ofPattern(
                "uuuu-mm-dd hh:mm a",
                Locale.ENGLISH
            )

            incident.time = LocalDateTime.parse(datetimeString, datetimeFormatter)
        }

        if (xCoord != null) {
            incident.location_x = xCoord
        }

        if (yCoord != null) {
            incident.location_y = yCoord
        }

        INCIDENT_REPO.save(incident)
    }


}