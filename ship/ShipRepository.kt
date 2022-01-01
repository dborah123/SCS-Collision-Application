package com.example.scscollision.ship

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ShipRepository: JpaRepository<Ship, Long>{

    @Query("SELECT s FROM Ship s WHERE "
    + "(:id is null OR s.id = :id) "
    + "AND (:name is null OR s.name = :name) "
    + "AND (:countryOfOrigin is null or s.countryOfOrigin = :countryOfOrigin) "
    )
    fun getSpecificShips(
        @Param("id") id: Long?,
        @Param("name") name: String?,
        @Param("countryOfOrigin") countryOfOrigin: String?,
    ): List<Ship>

    @Query("SELECT s FROM Ship s WHERE s.name = :name")
    fun getShipByName(
        @Param("name") name: String
    ): Ship?

    @Query("SELECT s FROM Ship s WHERE s.name = :name")
    fun getShipByNameOptional(
        @Param("name") name: String
    ): Optional<Ship>

    @Query("SELECT s FROM Ship s WHERE "
    + "(:id is null AND s.name = :name) "
    + "OR (:name is null AND s.id = :id) "
    + "OR (s.name = :name AND s.id = :id)"
    )
    fun getShipsByIdOrName(
        @Param("id") id: Long?,
        @Param("name") name: String?
    ): List<Ship>
}