package com.ribbontek.neo4jdemo.dto

import java.util.Optional

data class AirportDto(
    val id: Int,
    val code: String?,
    val name: String,
    val city: String?,
    val country: String?,
    val countryCode: String?,
    val outgoingFlights: List<FlightDto>
)

data class FlightDto(
    val id: Int,
    val uid: Int,
    val srcDate: String,
    val srcTime: String,
    val destId: Int,
    val distance: Int,
    val code: String
)

data class AirportChat(
    val message: String
)

data class AirportChatResponse(
    val message: String,
    val reply: String
)

data class AirportSearchResult(
    val primary: Optional<MutableMap<String, Any>>,
    val related: List<EmbeddingsAirportResult>
)

data class EmbeddingsAirportResult(
    val id: Long,
    val name: String,
    val country: String
)
