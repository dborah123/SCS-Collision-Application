package com.example.scscollision.incident

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface IncidentRepository: JpaRepository<Incident, Long> {

    @Query("SELECT i from Incident i WHERE "
    + "(:id is null OR i.id = :id) "
    + "OR (:aId is null OR i.shipA = :aId OR i.shipB = :aId) "
    + "OR (:bId is null OR i.shipA = :bId OR i.shipB = :bId)"
    )
    fun getIncidents(
        @Param("id") id: Long?,
        @Param("aId") aId: Long?,
        @Param("bId") bId:Long?
    ): List<Incident>
}