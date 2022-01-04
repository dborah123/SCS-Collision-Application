package com.example.scscollision.incident

import com.example.scscollision.country.Country
import com.example.scscollision.ship.Ship
import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "incident")
class Incident(
                @Id
                @SequenceGenerator(
                    name = "incident_sequence",
                    sequenceName = "incident_sequence",
                    allocationSize = 1
                )
                @GeneratedValue(
                    strategy = GenerationType.SEQUENCE,
                    generator = "incident_sequence",
                )
                val id: Long? = null,
                @OneToOne
                val shipA: Ship,
                @OneToOne
                val shipB: Ship,
                val time: LocalDateTime,
                val location_x: Double,
                val location_y: Double,
                @JsonIgnore
                @ManyToMany(mappedBy = "incidents", fetch = FetchType.LAZY)
                val countriesInvolved: Set<Country>
                ) {

    override fun toString(): String {
        return "Time: ${time}\tIncident between: " +
                "${shipA.name} of ${shipA.countryOfOrigin} " +
                "and ${shipB.name} of ${shipB.countryOfOrigin}"
    }
}