package com.example.scscollision.country

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CountryRepository: JpaRepository<Country, Long> {
}