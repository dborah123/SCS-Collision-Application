package com.example.scscollision.country

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CountryRepository: JpaRepository<Country, Long> {

    @Query("SELECT c FROM Country c WHERE "
    + "(:id is null OR c.id = :id) "
    + "AND (:name is null OR c.name = :id)"
    )
    fun getSpecificBooks(
        @Param("id") id: Long?,
        @Param("name") name: String?
    ): List<Country>

    @Query("SELECT c FROM Country c WHERE c.name = :name")
    fun getCountryByName(
        @Param("name") name: String
    ): Country?

    @Query("SELECT c FROM Country c WHERE "
    + "c.numIncidents > :numIncidents"
    )
    fun gtNumIncidents(
        @Param("numIncidents") numIncidents: Int,
    ): List<Country>

    @Query("SELECT c FROM Country c WHERE "
    + "c.numIncidents < :numIncidents"
    )
    fun ltNumIncidents(
        @Param("numIncidents") numIncidents: Int,
    ): List<Country>

    @Query("SELECT c FROM Country c WHERE "
            + "c.numIncidents = :numIncidents"
    )
    fun eqNumIncidents(
        @Param("numIncidents") numIncidents: Int,
    ): List<Country>
}