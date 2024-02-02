package com.ribbontek.neo4jdemo.repository

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "planes")
class PlaneEntity(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val plid: Long? = null,
    var name: String? = null,
    var abbr: String? = null,
    var speed: Double? = null,
    var publicPlane: String = "N",
    var iata: String? = null,
    var icao: String? = null,
    var frequency: Int? = null
)
