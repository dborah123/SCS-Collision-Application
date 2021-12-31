package com.example.scscollision.ship

import com.example.scscollision.country.Country
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
            var x_coords: Double,
            var y_coords: Double,
            val countryOfOrigin: String){

    override fun toString(): String {
        return "$name @ x: $x_coords y: $y_coords from $countryOfOrigin"
    }

    fun getDistFromCoords(x_coords: Double, y_coords: Double): Double {
        /**
         * Finds the distance between this ship and coordinates. Returns a double
         */
        return sqrt(((this.x_coords - x_coords).pow(2))
                + ((this.y_coords - y_coords).pow(2)))
    }

    fun getDistBetweenShips(otherShip: Ship): Double {
        /**
         * Finds the distance between this ship and another. Returns a double
         */
        return sqrt(((this.x_coords - otherShip.x_coords).pow(2))
                + ((this.y_coords - otherShip.y_coords).pow(2)))
    }
}