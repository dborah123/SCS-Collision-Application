package com.example.scscollision.country

import com.example.scscollision.incident.Incident
import com.example.scscollision.ship.Ship
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
    val id: Long? = null,

    var name: String,
    var numShips: Int = 0,
    var numIncidents:Int = 0,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "incidents_countriesInvolved",
        joinColumns = [JoinColumn(name = "country_id")],
        inverseJoinColumns = [JoinColumn(name = "incident_id")]
    )
    var incidents: MutableList<Incident> = mutableListOf(),

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var ships: MutableSet<Ship> = mutableSetOf()) {


    fun addShip(ship: Ship) {
        ships.add(ship)
        numShips++
    }

    fun removeShip(ship: Ship) {
        ships.remove(ship)
        numShips--
    }

    fun addIncident(incident: Incident) {
        incidents.add(incident)
        numIncidents++
    }

    fun removeIncident(incident: Incident) {
        incidents.remove(incident)
        numIncidents--
    }

    // toString, Getters, Setters
    override fun toString(): String {
        return "Country: $name\t# of Ships: $numShips\t# of incidents: $numIncidents"
    }
}