package com.ribbontek.neo4jdemo.repository

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "airlines")
class AirlineEntity(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val alid: Long? = null,
    var name: String?,
    var iata: String? = null,
    var icao: String? = null,
    var callsign: String? = null,
    var country: String? = null,
    @Column(name = "country_code")
    var countryCode: String? = null,
    var uid: Long? = null,
    var alias: String? = null,
    var mode: String = "F", // char(1) default 'F'
    var active: String = "N", // varchar(1) default 'N'
    var source: String? = null,
    var frequency: Int? = 0
)
