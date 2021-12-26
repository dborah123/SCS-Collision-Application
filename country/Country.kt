package com.example.scscollision.country

import com.example.scscollision.incident.Incident
import javax.persistence.*

@Entity
@Table(name = "country")
class Country(
                @Id
                @SequenceGenerator(
                    name = "country_sequence",
                    sequenceName = "country_sequence",
                    allocationSize = 1
                )
                @GeneratedValue(
                    strategy = GenerationType.SEQUENCE,
                    generator = "country_sequence",
                )
                val id: Long = 0,
                val name: String,
                var numShips: Int,
                var numIncidents:Int = 0,
                var incidents) {


    // toString, Getters, Setters
    override fun toString(): String {
        return "Country: $name\t# of Ships: $numShips\t# of incidents: $numIncidents"
    }
}