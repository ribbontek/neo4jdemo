package com.ribbontek.neo4jdemo.service

import com.ribbontek.neo4jdemo.graph.repository.AirportNeo4jRepository
import com.ribbontek.neo4jdemo.mapping.toAirport
import com.ribbontek.neo4jdemo.mapping.toFlight
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface AirportNodeGeneratorService {
    fun generate()
}

@Service
class AirportNodeGeneratorServiceImpl(
    private val airportService: AirportService,
    private val airportNeo4jRepository: AirportNeo4jRepository
) : AirportNodeGeneratorService {

    /**
     * Generates 10 primary airport nodes & flight relationships in Neo4j for demo purposes retrieved from the mysql DB
     * we need this limit otherwise we'll get an out of memory exception with the amount of data being loaded into the embedded neo4j instance
     */
    @Transactional(transactionManager = "neo4jTransactionManager")
    override fun generate() {
        if (airportNeo4jRepository.count() > 0) return
        val idToAirportDtoMap = airportService.retrieveAirportDtos()
            .filter { it.outgoingFlights.isNotEmpty() }
            .sortedByDescending { it.outgoingFlights.size }
            .associateBy { it.id }
        val idToAirportMap = idToAirportDtoMap.values.associate { it.id to it.toAirport() }
        idToAirportMap.entries.take(10).forEach { (id, airport) ->
            val airportDto = idToAirportDtoMap[id]
            airport.apply {
                flights = airportDto
                    ?.outgoingFlights
                    ?.mapNotNull { flight -> idToAirportMap[flight.destId]?.let { flight.toFlight(it) } }
                    ?.toMutableList()
                    ?: mutableListOf()
            }.takeIf {
                it.flights.isNotEmpty()
            }?.let {
                airportNeo4jRepository.save(it)
            }
        }
    }
}
