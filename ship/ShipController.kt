package com.example.scscollision.ship

import com.example.scscollision.country.Country
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path= ["ships"])
class ShipController(@Autowired val SHIP_SERVICE: ShipService, ) {

    /****************
     * GET MAPPINGS *
     ****************/

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

    @GetMapping("/search/radius/")
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

    @GetMapping("/search/near/")
    fun getDistBetween(
        @RequestParam() shipAId: Long,
        @RequestParam() shipBId: Long
    ): Double {
        return SHIP_SERVICE.getDistBetween(shipAId, shipBId)
    }

    @GetMapping("/search/nearest/")
    fun getNearestShip(
        @RequestParam(required = false) shipId: Long?,
        @RequestParam(required = false) name: String?
    ): Ship? {
        return SHIP_SERVICE.getNearestShip(shipId, name)
    }

    /*****************
     * POST MAPPINGS *
     *****************/

    @PostMapping("/add/")
    fun addShip(
        @RequestBody() ship: Ship
    ) {
        SHIP_SERVICE.addShip(ship)
    }

    /****************
     * PUT MAPPINGS *
     ****************/

    @PutMapping("/update/")
    fun updateShip(
        @RequestParam(required = false) id: Long?,
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) newName: String?,
        @RequestParam(required = false) newCountryOfOrigin: String?
    ) {
        return SHIP_SERVICE.updateShip(id, name, newName, newCountryOfOrigin)
    }

    @PutMapping("/update/coordinates/")
    fun updateCoords(
        @RequestParam(required = false) id: Long?,
        @RequestParam(required = false) name: String?,
        @RequestParam() xCoord: Double,
        @RequestParam() yCoord: Double
    ) {
        return SHIP_SERVICE.updateCoords(id, name, xCoord, yCoord)
    }

    /*******************
     * DELETE MAPPINGS *
     *******************/

    @DeleteMapping("/delete/")
    fun deleteShip(
        @RequestParam(required = false) id: Long?,
        @RequestParam(required = false) name: String?
    ) {
        return SHIP_SERVICE.deleteShip(id, name)
    }

    /******************
     * CLOSEST POINTS *
     ******************/

    @GetMapping("/monitor/")
    fun closestPoints(): Triple<Ship, Ship, Double>? {
        return SHIP_SERVICE.closestPoints()
    }
}
