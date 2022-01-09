package com.example.scscollision.ship

import com.example.scscollision.country.Country
import com.example.scscollision.country.CountryRepository
import com.example.scscollision.incident.Incident
import com.example.scscollision.incident.IncidentRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime

@Configuration
class ShipConfig {

    @Bean
    fun run(shiprepo: ShipRepository, countryrepo: CountryRepository, incidentrepo: IncidentRepository): CommandLineRunner {
        return CommandLineRunner { args ->
            val usa: Country = Country(name = "United States of America")
            val china: Country = Country(name = "China")

            val shipA: Ship = Ship(
                name = "USS Arizona",
                xCoord = 153122.0,
                yCoord = 1143003.6,
                countryOfOrigin = "United States of America"
            )
            val shipB: Ship = Ship(
                name = "JS2",
                xCoord = 153122.0,
                yCoord = 1143003.6,
                countryOfOrigin = "China"
            )
            usa.addShip(shipA)
            china.addShip(shipB)

            val incident = Incident(
                shipA = shipA,
                shipB = shipB,
                time = LocalDateTime.now(),
                location_x = shipA.xCoord,
                location_y = shipA.yCoord,
                countriesInvolved = mutableSetOf(usa, china)
            )
            countryrepo.saveAll(listOf(usa, china))
            shiprepo.saveAll(listOf(shipA, shipB))

            incidentrepo.save(incident)

            usa.addIncident(incident)
            china.addIncident(incident)

            countryrepo.saveAll(listOf(usa, china))



        }
    }
}