package com.example.scscollision.incident

import com.example.scscollision.ship.Ship
import java.time.LocalDateTime

class Incident(val shipA: Ship, val shipB: Ship, val time: LocalDateTime) {

    override fun toString(): String {
        return "Time: ${time}\tIncident between: ${shipA.name} of ${shipA.countryOfOrigin.name} and ${shipB.name} of ${shipB.countryOfOrigin.name}"
    }
}