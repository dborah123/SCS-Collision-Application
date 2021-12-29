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

    @GetMapping
    fun getShips(): List<Ship> {
        /*
         * Gets all ships in database
         */
        return SHIP_SERVICE.getShips()
    }

    @GetMapping("/search/")
    fun getSpecificShips(
        @RequestParam(required = false) id: Long?,
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) countryOfOrigin: String?,
    ): List<Ship> {
        /*
         * Searches ships with optional parameters: id, name, country of origin
         */
        return SHIP_SERVICE.getSpecificShips(id, name, countryOfOrigin)
    }

    @GetMapping("/search/radius")
    fun getShipsInRadius(
        @RequestParam() x_coord: Double,
        @RequestParam() y_coord: Double,
        @RequestParam() radius: Double,
    ): MutableList<Ship> {
        /*
         * Gets ships within a specific radius of a specified point
         */
        return SHIP_SERVICE.getShipsInRadius(x_coord, y_coord, radius)
    }
}
