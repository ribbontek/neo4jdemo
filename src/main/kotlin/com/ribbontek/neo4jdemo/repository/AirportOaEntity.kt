package com.ribbontek.neo4jdemo.repository

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "airports_oa")
class AirportOaEntity(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val oaid: Long? = null,
    var ident: String? = null,
    var type: String? = null,
    var name: String? = null,
    var x: Double = 0.0,
    var y: Double = 0.0,
    var elevation: Int? = null,
    var continent: String? = null,
    var country: String? = null,
    var region: String? = null,
    var city: String? = null,
    var service: String? = null,
    var icao: String? = null,
    var iata: String? = null,
    var local: String? = null,
    var home_link: String? = null,
    var wp_link: String? = null,
    var keywords: String? = null
)
