package com.example.scscollision.ship

import com.example.scscollision.country.Country
import com.example.scscollision.country.CountryRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ShipConfig {

    @Bean
    fun run(shiprepo: ShipRepository, countryrepo: CountryRepository): CommandLineRunner {
        return CommandLineRunner { args ->
            val usa: Country = Country(name = "United States of America")
            val china: Country = Country(name = "China")
            val shipA: Ship = Ship(
                name = "USS Arizona",
                xCoord = 153122.0,
                yCoord = 1143003.6,
                countryOfOrigin = "United States of America"
            )
            usa.addShip(shipA)
            countryrepo.saveAll(listOf(usa, china))
            shiprepo.saveAll(listOf(shipA))

        }
    }
}