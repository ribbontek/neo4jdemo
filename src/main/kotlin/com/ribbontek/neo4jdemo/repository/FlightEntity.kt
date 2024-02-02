package com.ribbontek.neo4jdemo.repository

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.sql.Date
import java.time.LocalDateTime

@Entity
@Table(name = "flights")
class FlightEntity(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val fid: Int? = null,
    var uid: Int,
    @Column(name = "src_apid")
    var srcApid: Int? = null,
    @Column(name = "src_date")
    var srcDate: Date,
    @Column(name = "src_time")
    var srcTime: String,
    @Column(name = "dst_apid")
    var dstApid: Int? = null,
    var distance: Int,
    var code: String,
    var seat: String? = null,
    @Column(name = "seat_type")
    var seatType: String? = null,
    @Column(name = "class")
    var flightClass: String? = null,
    var reason: String? = null,
    var plid: Long? = null,
    var alid: Long? = null,
    var trid: Long? = null,
    var duration: String? = null,
    var registration: String? = null,
    var note: String? = null,
    @Column(name = "upd_time", columnDefinition = "datetime")
    var updTime: LocalDateTime? = null,
    var opp: String = "N",
    var mode: String = "F"
)
