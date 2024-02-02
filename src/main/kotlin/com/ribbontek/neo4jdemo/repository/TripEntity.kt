package com.ribbontek.neo4jdemo.repository

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "trips")
class TripEntity(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    var trid: Long? = null,
    var name: String? = null,
    var url: String? = null,
    var uid: Long? = null,
    var publicTrip: String? = null
)
