package com.ribbontek.neo4jdemo.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AirportRepository : JpaRepository<AirportEntity, Int>
