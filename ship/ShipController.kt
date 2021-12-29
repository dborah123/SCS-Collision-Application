package com.example.scscollision.ship

import com.example.scscollision.country.Country
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path= ["ships"])
class ShipController(@Autowired val SHIP_SERVICE: ShipService, ) {

    @GetMapping("/search/")
    fun getShips(
                    @RequestParam(required = false) id: Long?,
                    @RequestParam(required = false) name: String,
                    @RequestParam(required = false) x_coords: Double,
                    @RequestParam(required = false) y_coords: Double,
                    @RequestParam(required = false) countryOfOrigin: String,
    ): List<Ship> {
        return SHIP_SERVICE.getShips()
    }
}