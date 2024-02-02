package com.ribbontek.neo4jdemo.repository

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "countries")
class CountryEntity(
    @Id
    val dafif_code: String? = null,
    var name: String? = null,
    var iso_code: String? = null
)
