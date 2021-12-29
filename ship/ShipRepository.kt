package com.example.scscollision.ship

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ShipRepository: JpaRepository<Ship, Long>{
}