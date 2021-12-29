package com.example.scscollision.ship

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

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
}