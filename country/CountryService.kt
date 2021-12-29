package com.example.scscollision.country

import com.example.scscollision.ship.ShipRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CountryService(@Autowired val COUNTRY_REPO: CountryRepository) {
}