package com.example.scscollision.incident

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface IncidentRepository: JpaRepository<Incident, Long> {

    @Query("SELECT i FROM Incident i WHERE "
    + "(:id is null OR i.id = :id) "
    + "AND (:aId is null OR i.shipA.id = :aId OR i.shipB.id = :aId) "
    + "AND (:bId is null OR i.shipA.id = :bId OR i.shipB.id = :bId)"
    )
    fun getIncidents(
        @Param("id") id: Long?,
        @Param("aId") aId: Long?,
        @Param("bId") bId:Long?
    ): List<Incident>

    @Query("SELECT i FROM Incident i WHERE "
    + "(:aId is null OR :aId MEMBER OF i.countriesInvolved) "
    + "AND (:bId is null OR :bId MEMBER OF i.countriesInvolved)"
    )
    fun getIncidentsByCountries(
        @Param("aId") aId: Long?,
        @Param("bId") bId: Long?,
    ): List<Incident>
}