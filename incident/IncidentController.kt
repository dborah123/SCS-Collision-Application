package com.example.scscollision.incident

import com.example.scscollision.country.Country
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.persistence.Id

@RestController
@RequestMapping(path=["incidents"])
class IncidentController(@Autowired val INCIDENT_SERVICE: IncidentService) {

    /****************
     * GET MAPPINGS *
     ****************/

    @GetMapping("/search/")
    fun getIncidents(
        @RequestParam(required = false) id: Long?,
        @RequestParam(required = false) shipAName: String?,
        @RequestParam(required = false) shipAId: Long?,
        @RequestParam(required = false) shipBName: String?,
        @RequestParam(required = false) shipBId: Long?,
    ): List<Incident> {
        return INCIDENT_SERVICE.getIncidents(id, shipAName, shipAId, shipBName, shipBId)
    }

    @GetMapping("/search/bycountry/")
    fun getIncidentsByCountry(
        @RequestParam(required = false) countryAId: Long?,
        @RequestParam(required = false) countryAName: String?,
        @RequestParam(required = false) countryBId: Long?,
        @RequestParam(required = false) countryBName: String?,
    ): List<Incident> {
        return INCIDENT_SERVICE.getIncidentsByCountry(countryAId,
            countryAName,
            countryBId,
            countryBName
        )
    }

    @GetMapping("/search/radius/")
    fun getIncidentsByRadius(
        @RequestParam xCoord: Double,
        @RequestParam yCoord: Double,
        @RequestParam radius: Double
    ): MutableList<Incident> {
        return INCIDENT_SERVICE.getIncidentsByRadius(xCoord, yCoord, radius)
    }

    /****************
     * POST MAPPING *
     ****************/

    @PostMapping("/add/")
    fun addIncident(
        @RequestParam(required = false) shipAId: Long?,
        @RequestParam(required = false) shipAName: String?,
        @RequestParam(required = false) shipBId: Long?,
        @RequestParam(required = false) shipBName: String?,
        @RequestParam(required = false) datetime: String?,
        @RequestParam(required = false) now: Boolean?
    ) {
        // TODO: Add better time functionalities
        return INCIDENT_SERVICE.addIncident(
            shipAId, shipAName, shipBId, shipBName, datetime, now
        )
    }
}