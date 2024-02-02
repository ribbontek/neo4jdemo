package com.ribbontek.neo4jdemo.repository

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "airports_dafif")
class AirportDafifEntity(
    @Id
    val icao: String? = null,
    var name: String? = null,
    var city: String? = null,
    var code: String? = null,
    var iata: String? = null,
    var x: Double = 0.0,
    var y: Double = 0.0,
    var elevation: Int? = null
)
