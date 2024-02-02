package com.ribbontek.neo4jdemo.graph.repository

import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository

@Repository
interface AirportNeo4jRepository : Neo4jRepository<Airport, Long>

@Repository
interface PlaneNeo4jRepository : Neo4jRepository<Plane, Long>
