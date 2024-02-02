package com.ribbontek.neo4jdemo.service

import com.ribbontek.neo4jdemo.dto.AirportDto
import com.ribbontek.neo4jdemo.mapping.toAirportDto
import com.ribbontek.neo4jdemo.mapping.toFlightDto
import com.ribbontek.neo4jdemo.repository.AirportRepository
import com.ribbontek.neo4jdemo.repository.FlightRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface AirportService {
    fun retrieveAirportDtos(): List<AirportDto>
}

@Service
class AirportServiceImpl(
    private val airportRepository: AirportRepository,
    private val flightRepository: FlightRepository
) : AirportService {

    /**
     * Retrieves all the airports & flight details
     */
    @Transactional(readOnly = true, transactionManager = "transactionManager")
    override fun retrieveAirportDtos(): List<AirportDto> {
        val flights = flightRepository.findAll()
        val flightsFrom = flights.groupBy { it.srcApid!! }
        return airportRepository.findAll()
            .mapNotNull { airportEntity ->
                airportEntity.apid?.let {
                    airportEntity.toAirportDto(
                        outgoingFlights = flightsFrom[it]?.map { it.toFlightDto() } ?: emptyList()
                    )
                }
            }
    }
}
