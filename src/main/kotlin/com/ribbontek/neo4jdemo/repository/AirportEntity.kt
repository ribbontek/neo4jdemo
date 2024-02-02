package com.ribbontek.neo4jdemo.repository

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "airports")
class AirportEntity(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val apid: Int? = null,
    var name: String,
    var city: String? = null,
    var country: String? = null,
    @Column(name = "country_code")
    var countryCode: String? = null,
    var iata: String? = null,
    var icao: String? = null,
    var x: Double = 0.0,
    var y: Double = 0.0,
    var elevation: Int? = null,
    var uid: Long? = null,
    var timezone: Float? = null,
    var dst: String? = null,
    @Column(name = "tz_id")
    var tzId: String? = null,
    var type: String? = null,
    var source: String? = null
)
