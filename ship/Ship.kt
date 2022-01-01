package com.example.scscollision.ship

import javax.persistence.*
import kotlin.math.pow
import kotlin.math.sqrt

@Entity
@Table(name="ship")
class Ship(
    @Id
            @SequenceGenerator(
                name = "ship_sequence",
                sequenceName = "ship_sequence",
                allocationSize = 1
            )
            @GeneratedValue(
                strategy = GenerationType.SEQUENCE,
                generator = "ship_sequence"
            )
            val id: Long? = null,
    var name: String,
    var xCoord: Double,
    var yCoord: Double,
    var countryOfOrigin: String){

    override fun toString(): String {
        return "$name @ x: $xCoord y: $yCoord from $countryOfOrigin"
    }

    fun getDistFromCoords(x_coords: Double, y_coords: Double): Double {
        /**
         * Finds the distance between this ship and coordinates. Returns a double
         */
        return sqrt(((this.xCoord - x_coords).pow(2))
                + ((this.yCoord - y_coords).pow(2)))
    }

    fun getDistBetweenShips(otherShip: Ship): Double {
        /**
         * Finds the distance between this ship and another. Returns a double
         */
        return sqrt(((this.xCoord - otherShip.xCoord).pow(2))
                + ((this.yCoord - otherShip.yCoord).pow(2)))
    }
}