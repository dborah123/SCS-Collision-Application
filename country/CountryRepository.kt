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
    fun getSpecificCountries(
        @Param("id") id: Long?,
        @Param("name") name: String?
    ): List<Country>

    @Query("SELECT c FROM Country c WHERE c.name = :name")
    fun getCountryByName(
        @Param("name") name: String
    ): Country?

    /**
     * Operator queries for number of incidents
     */
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

    /**
     * Operator functions for number of ships
     */
    @Query(
        value = "SELECT * FROM COUNTRY c WHERE c.num_ships > :numShips",
        nativeQuery = true
    )
    fun gtNumShips(
        @Param("numShips") numShips: Int
    ): List<Country>

    @Query(
        value = "SELECT * FROM COUNTRY c WHERE c.num_ships < :numShips",
        nativeQuery = true
    )
    fun ltNumShips(
        @Param("numShips") numShips: Int
    ): List<Country>

    @Query(
        value = "SELECT * FROM COUNTRY c WHERE c.num_ships = :numShips",
        nativeQuery = true
    )
    fun eqNumShips(
        @Param("numShips") numShips: Int
    ): List<Country>


}