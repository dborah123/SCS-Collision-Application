package com.example.scscollision.ship

import com.example.scscollision.country.Country
import com.example.scscollision.country.CountryRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class ShipConfig {

    @Bean
    fun run(shiprepo: ShipRepository, countryrepo: CountryRepository): CommandLineRunner {
        return CommandLineRunner { args ->
            val usa: Country = Country(name = "United States of America")
            val shipA: Ship = Ship(
                name = "USS Arizona",
                x_coords = 153122.0,
                y_coords = 1143003.6,
                countryOfOrigin = usa
            )

            countryrepo.saveAll(listOf(usa))
            shiprepo.saveAll(listOf(shipA))

        }
    }
}