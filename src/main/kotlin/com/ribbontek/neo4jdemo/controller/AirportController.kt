package com.ribbontek.neo4jdemo.controller

import com.ribbontek.neo4jdemo.dto.AirportChat
import com.ribbontek.neo4jdemo.dto.AirportChatResponse
import com.ribbontek.neo4jdemo.service.AirportGenAIService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AirportController(
    private val airportGenAIService: AirportGenAIService
) {
    @PostMapping("/airports/ask")
    fun askQuestion(askChat: AirportChat): AirportChatResponse {
        return airportGenAIService.askQuestion(askChat)
    }
}
