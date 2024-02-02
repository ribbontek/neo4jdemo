package com.ribbontek.neo4jdemo.repository

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "countries_oa")
class CountryOaEntity(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val oacode: Long? = null,
    var country: String? = null,
    var continent: String? = null
)
