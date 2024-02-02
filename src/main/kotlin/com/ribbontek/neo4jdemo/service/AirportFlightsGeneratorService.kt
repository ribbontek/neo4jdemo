package com.ribbontek.neo4jdemo.service

import com.ribbontek.neo4jdemo.repository.FlightEntity
import com.ribbontek.neo4jdemo.repository.FlightRepository
import com.ribbontek.neo4jdemo.repository.RouteRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.Date
import java.sql.Time
import java.util.Calendar
import kotlin.random.Random

interface AirportFlightsGeneratorService {
    fun generate()
}

@Service
class AirportFlightsGeneratorServiceImpl(
    private val routeRepository: RouteRepository,
    private val flightRepository: FlightRepository
) : AirportFlightsGeneratorService {

    /**
     * Generates random flight data over the past year based on the routes
     */
    @Transactional(transactionManager = "transactionManager")
    override fun generate() {
        if (flightRepository.count() > 0) return
        val routes = routeRepository.findAll()
        val flights = routes.map { route ->
            val time = randomTimeWithinYear()
            FlightEntity(
                uid = Random.nextInt(from = 1, until = Int.MAX_VALUE),
                srcApid = route.srcApid,
                srcDate = Date(time.time),
                srcTime = time.toString(),
                dstApid = route.dstApid,
                distance = Random.nextInt(1000, 20000),
                code = generateRandomAlphanumeric(6)
            )
        }
        flightRepository.saveAll(flights)
    }

    private fun randomTimeWithinYear(): Time {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, -1)
        val randomTimeInMillis = calendar.timeInMillis + Random.nextLong(System.currentTimeMillis() - calendar.timeInMillis)
        return Time(randomTimeInMillis)
    }

    private fun generateRandomAlphanumeric(length: Int): String {
        val alphanumericChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        val random = Random.Default

        return (1..length)
            .map { alphanumericChars[random.nextInt(alphanumericChars.length)] }
            .joinToString("")
    }
}
