package com.ribbontek.neo4jdemo.config

import com.ribbontek.neo4jdemo.dto.AirportChat
import com.ribbontek.neo4jdemo.service.AirportFlightsGeneratorService
import com.ribbontek.neo4jdemo.service.AirportGenAIService
import com.ribbontek.neo4jdemo.service.AirportNodeGeneratorService
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

@Component
class AppBootstrap(
    private val airportFlightsGeneratorService: AirportFlightsGeneratorService,
    private val airportNodeGeneratorService: AirportNodeGeneratorService,
    private val airportGenAIService: AirportGenAIService,
    private val environment: Environment
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @EventListener
    fun bootstrap(event: ApplicationReadyEvent) {
        log.info("Starting bootstrap")
        airportFlightsGeneratorService.generate()
        airportNodeGeneratorService.generate()
        // TODO: Uncomment the ones you wish to test out
        // demoOpenAiCompletions()
        // demoOpenAiCompletionsGuarded()
        // demoOpenAiCipherQueryGeneration()
        // demoOpenAiCipherQueryGenAndEmbeddingsSearch()
        log.info("Finished bootstrap")
    }

    /**
     *  Demo OpenAI Completions
     *
     *  Examples that get generated:
     *
     *  DEMO 1
     *  QUESTION: What country has the most flights for an airport?
     *  RESPONSE: The United States has the most flights for an airport with Hartsfield-Jackson Atlanta International Airport in Atlanta, Georgia being the busiest airport in terms of number of flights.
     *
     *  DEMO 2
     *  QUESTION: What is the capital of Australia?
     *  RESPONSE: Canberra
     *
     */
    fun demoOpenAiCompletions() {
        DEMO_QUESTIONS.forEachIndexed { index, question ->
            val response = airportGenAIService.askQuestion(AirportChat(question))
            log.info(
                """ DEMO ${index + 1}
                    QUESTION: $question
                    RESPONSE: ${response.reply}
                """.trimIndent()
            )
        }
    }

    /**
     *  Demo OpenAI Completions with Prompt Guards
     *
     *  Examples that get generated:
     *
     *  DEMO 1
     *  QUESTION: What country has the most flights for an airport?
     *  RESPONSE: The country with the most flights for an airport varies depending on the specific airport.
     *
     *  DEMO 2
     *  QUESTION: What is the capital of Australia?
     *  RESPONSE: I'm sorry, that question is not related to airports or flights. Please ask a question related to airports or flights.
     */
    fun demoOpenAiCompletionsGuarded() {
        DEMO_QUESTIONS.forEachIndexed { index, question ->
            val response = airportGenAIService.askQuestionGuarded(AirportChat(question))
            log.info(
                """ DEMO ${index + 1}
                    QUESTION: $question
                    RESPONSE: ${response.reply}
                """.trimIndent()
            )
        }
    }

    /**
     * Demo OpenAI Cipher Query Generation
     *
     * Examples that get generated:
     *
     * DEMO 1
     * QUESTION: What country has the most flights for an airport?
     * RESPONSE: MATCH (a:Airport)-[:HAS_FLIGHTS]->(f)
     * WITH a.country AS country, COUNT(f) AS numFlights
     * ORDER BY numFlights DESC
     * RETURN country
     * LIMIT 1
     *
     * DEMO 2
     * QUESTION: What is the capital of Australia?
     * RESPONSE: MATCH (a:Airport)
     * WHERE a.country = "Australia" AND a.capital IS NOT NULL
     * RETURN a.capital
     *
     */
    fun demoOpenAiCipherQueryGeneration() {
        DEMO_QUESTIONS.forEachIndexed { index, question ->
            val response = airportGenAIService.testEmbeddedNeo4jCipherGeneration(AirportChat(question))
            log.info(
                """ DEMO ${index + 1}
                    QUESTION: $question
                    RESPONSE: ${response.reply}
                """.trimIndent()
            )
        }
    }

    /**
     * Demo OpenAI Cipher Query Gen & Embeddings Search
     *
     * Examples that get generated:
     *
     * DEMO EMBEDDINGS SEARCH
     * QUESTION: What country has the most flights for an airport?
     * RESPONSE: AirportSearchResult(primary=Optional[{country=United States}], related=[EmbeddingsAirportResult(id=2832, name=Shanghai Hongqiao International Airport, country=China), EmbeddingsAirportResult(id=3394, name=Longdongbao Airport, country=China), EmbeddingsAirportResult(id=2021, name=Alama Iqbal International Airport, country=Pakistan), EmbeddingsAirportResult(id=3369, name=Taiyuan Wusu Airport, country=China), EmbeddingsAirportResult(id=2732, name=Zhongwei Shapotou Airport, country=China)])
     */
    fun demoOpenAiCipherQueryGenAndEmbeddingsSearch() {
        if (!environment.activeProfiles.contains("neo4jexternal")) {
            log.info("Must use the external neo4j instance running in docker-compose with the GDS functions available")
            return
        }
        val question = "What country has the most flights for an airport?"
        val response = airportGenAIService.embeddingsVectorAirportSearch(AirportChat(question))
        log.info(
            """ DEMO EMBEDDINGS SEARCH
                QUESTION: $question
                RESPONSE: $response
            """.trimIndent()
        )
    }

    companion object {
        private val DEMO_QUESTIONS = listOf("What country has the most flights for an airport?", "What is the capital of Australia?")
    }
}
