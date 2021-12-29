package com.example.scscollision.country

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

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
}