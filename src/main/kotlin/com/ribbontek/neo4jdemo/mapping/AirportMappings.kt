package com.ribbontek.neo4jdemo.mapping

import com.ribbontek.neo4jdemo.dto.AirportDto
import com.ribbontek.neo4jdemo.dto.FlightDto
import com.ribbontek.neo4jdemo.graph.repository.Airport
import com.ribbontek.neo4jdemo.graph.repository.Flight
import com.ribbontek.neo4jdemo.repository.AirportEntity
import com.ribbontek.neo4jdemo.repository.FlightEntity

fun AirportEntity.toAirportDto(
    outgoingFlights: List<FlightDto>
): AirportDto {
    return AirportDto(
        id = apid!!,
        code = icao,
        name = name,
        city = city,
        country = country,
        countryCode = countryCode,
        outgoingFlights = outgoingFlights
    )
}

fun FlightEntity.toFlightDto(): FlightDto {
    return FlightDto(
        id = fid!!,
        uid = uid,
        srcDate = srcDate.toLocalDate().toString(),
        srcTime = srcTime,
        destId = dstApid!!,
        distance = distance,
        code = code
    )
}

fun AirportDto.toAirport(): Airport {
    return Airport(
        id = id.toLong(),
        code = code,
        name = name,
        city = city,
        country = country,
        countryCode = countryCode
    )
}

fun FlightDto.toFlight(target: Airport): Flight {
    return Flight(
        airport = target,
        code = code,
        distance = distance,
        currency = null,
        durationAvg = null,
        planeId = null,
        price = null
    )
}
