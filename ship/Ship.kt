package com.example.scscollision.ship

import com.example.scscollision.country.Country
import javax.persistence.*

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
            val id: Long = 0,

            @Column
            var name: String,

            @Column
            var x_coords: Double,

            @Column
            var y_coords: Double,

            @ManyToMany()
            val countryOfOrigin: Country){

    override fun toString(): String {
        return ""
    }

}