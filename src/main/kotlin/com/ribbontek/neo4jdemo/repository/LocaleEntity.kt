package com.ribbontek.neo4jdemo.repository

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "locales")
class LocaleEntity {
    @Id
    val locale: String? = null
    var name: String? = null
}
