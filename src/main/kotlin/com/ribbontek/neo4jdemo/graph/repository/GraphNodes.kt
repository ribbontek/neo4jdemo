package com.ribbontek.neo4jdemo.graph.repository

import org.springframework.data.neo4j.core.schema.GeneratedValue
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship
import org.springframework.data.neo4j.core.schema.RelationshipId
import org.springframework.data.neo4j.core.schema.RelationshipProperties
import org.springframework.data.neo4j.core.schema.TargetNode
import java.math.BigDecimal

@Node("Airport")
class Airport(
    @Id
    @GeneratedValue
    var id: Long? = null,
    val code: String?,
    val name: String,
    val city: String?,
    val country: String?,
    val countryCode: String?,
    @Relationship(type = "HAS_FLIGHTS")
    var flights: MutableList<Flight> = mutableListOf()
)

@RelationshipProperties
class Flight(
    @RelationshipId
    var id: Long? = null,
    @TargetNode
    var airport: Airport,
    var code: String?,
    var price: BigDecimal?,
    var currency: String?,
    var distance: Int?,
    var durationAvg: Long?,
    var planeId: Long?
)

@Node("Plane")
class Plane(
    @Id
    @GeneratedValue
    var id: Long? = null,
    var name: String,
    var abbr: String,
    var frequency: Int,
    @Relationship(type = "ROUTE_TO", direction = Relationship.Direction.OUTGOING)
    var route: Route
)

@RelationshipProperties
class Route(
    @RelationshipId
    var id: Long? = null,
    var departure: Airport,
    @TargetNode
    var arrival: Airport
)
