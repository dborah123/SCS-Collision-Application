package com.example.scscollision.incident

import com.example.scscollision.country.Country
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
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
    fun getIncidentByCountry(
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
}