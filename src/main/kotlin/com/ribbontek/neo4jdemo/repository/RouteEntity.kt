package com.ribbontek.neo4jdemo.repository

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "routes")
class RouteEntity(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val rid: Int? = null,
    var airline: String? = null,
    var alid: Long? = null,
    @Column(name = "src_ap")
    var srcAp: String? = null,
    @Column(name = "src_apid")
    var srcApid: Int? = null,
    @Column(name = "dst_ap")
    var dstAp: String? = null,
    @Column(name = "dst_apid")
    var dstApid: Int? = null,
    var codeshare: String? = null,
    var stops: String? = null,
    var equipment: String? = null,
    var added: String? = null
)
