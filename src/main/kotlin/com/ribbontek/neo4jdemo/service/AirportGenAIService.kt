package com.ribbontek.neo4jdemo.service

import com.ribbontek.neo4jdemo.dto.AirportChat
import com.ribbontek.neo4jdemo.dto.AirportChatResponse
import com.ribbontek.neo4jdemo.dto.AirportSearchResult
import com.ribbontek.neo4jdemo.dto.EmbeddingsAirportResult
import com.ribbontek.neo4jdemo.graph.repository.AirportNeo4jRepository
import com.ribbontek.neo4jdemo.models.Visualization
import com.ribbontek.neo4jdemo.models.VisualizationNode
import com.ribbontek.neo4jdemo.models.VisualizationRelationship
import com.ribbontek.neo4jdemo.openai.CompletionMessage
import com.ribbontek.neo4jdemo.openai.CompletionRequest
import com.ribbontek.neo4jdemo.openai.EmbeddingsRequest
import com.ribbontek.neo4jdemo.openai.OpenAIClient
import com.ribbontek.neo4jdemo.prompt.PromptTemplate
import org.neo4j.driver.Values
import org.slf4j.LoggerFactory
import org.springframework.data.neo4j.core.Neo4jClient
import org.springframework.data.neo4j.core.fetchAs
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

interface AirportGenAIService {
    fun askQuestion(askChat: AirportChat): AirportChatResponse
    fun askQuestionGuarded(askChat: AirportChat): AirportChatResponse
    fun testEmbeddedNeo4jCipherGeneration(askChat: AirportChat): AirportChatResponse
    fun embeddingsVectorAirportSearch(askChat: AirportChat): AirportSearchResult
}

@Service
class AirportGenAIServiceImpl(
    private val neo4jClient: Neo4jClient,
    private val openAIClient: OpenAIClient,
    private val airportNeo4jRepository: AirportNeo4jRepository
) : AirportGenAIService {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Allow the user to ask the API a question about the airports using openai
     */
    override fun askQuestion(askChat: AirportChat): AirportChatResponse {
        val response = openAIClient.getCompletion(
            CompletionRequest(
                messages = listOf(CompletionMessage(content = askChat.message))
            )
        )
        return response.choices.takeIf { it.isNotEmpty() }?.firstOrNull()?.let {
            AirportChatResponse(
                message = askChat.message,
                reply = it.message.content
            )
        } ?: throw IllegalStateException("Throw an API exception")
    }

    /**
     * Allow the user to ask the API a question about the airports using openai with a guard filter
     */
    override fun askQuestionGuarded(askChat: AirportChat): AirportChatResponse {
        val prompt = PromptTemplate.createInitialPrompt(askChat.message)
        val response = openAIClient.getCompletion(
            CompletionRequest(
                messages = listOf(CompletionMessage(content = prompt))
            )
        )
        return response.choices.takeIf { it.isNotEmpty() }?.firstOrNull()?.let {
            AirportChatResponse(
                message = askChat.message,
                reply = FINAL_ANSWER_PATTERN.find(it.message.content)?.groupValues?.lastOrNull()
                    ?: it.message.content
            )
        } ?: throw IllegalStateException("Throw an API exception")
    }

    /**
     * Allow the user to ask the API a question about the airports using OpenAI to generate a query cipher to use in Neo4j
     */
    override fun testEmbeddedNeo4jCipherGeneration(askChat: AirportChat): AirportChatResponse {
        val visualization = retrieveVisualization()
        log.info("Found visualization >> $visualization")
        val prompt = PromptTemplate.createCypherQuery(visualization.toString(), askChat.message)
        val response = openAIClient.getCompletion(
            CompletionRequest(
                messages = listOf(CompletionMessage(content = prompt))
            )
        )
        return response.choices.takeIf { it.isNotEmpty() }?.firstOrNull()?.let {
            AirportChatResponse(
                message = askChat.message,
                reply = it.message.content
            )
        } ?: throw IllegalStateException("Throw an API exception")
    }

    /**
     * Allow the user to use OpenAI to generate a cipher query, query the DB, retrieve embeddings from the initial response, to find similar nodes
     *
     * The initial query result & embeddings response can be used to embellish chat functionality with more context
     */
    override fun embeddingsVectorAirportSearch(askChat: AirportChat): AirportSearchResult {
        createEmbeddingsForAirports()
        // cypher query generation is not 100% reliable - sometimes uses the MAX query & this crashes
        val cipherQueryResponse = testEmbeddedNeo4jCipherGeneration(askChat)
        val primaryQueryResult = neo4jClient.query(cipherQueryResponse.reply).fetch().one()
        log.info("Found primary query result: $primaryQueryResult")
        val embeddingsResponse = openAIClient.getEmbeddings(EmbeddingsRequest(cipherQueryResponse.reply, "text-embedding-3-small", dimensions = 50))
        val embeddings = embeddingsResponse.data.firstOrNull()?.embedding ?: throw IllegalStateException("No embeddings")
        log.info("Found embeddings >> $embeddings")
        // finds related 5 records
        val relatedQueryResults = neo4jClient.query(
            "MATCH (a:Airport) WITH a, gds.similarity.cosine(a.node2vecEmbedding, \$embeddings) AS similarity RETURN a ORDER BY similarity DESC LIMIT \$total;"
        )
            .bindAll(mapOf("embeddings" to embeddings.map { Values.value(it.toDouble()) }, "total" to Values.value(5)))
            .fetchAs<AirportNode>()
            .mappedBy { _, record ->
                AirportNode(id = record.get("a").asNode().id())
            }
            .all()
        val relatedAirports = airportNeo4jRepository.findAllById(relatedQueryResults.map { it.id })
            .map { EmbeddingsAirportResult(it.id!!, it.name, it.country!!) }
        return AirportSearchResult(
            primary = primaryQueryResult,
            related = relatedAirports
        )
    }

    private data class AirportNode(
        val id: Long? = null
    )

    // Run node2vec algorithm to generate embeddings (gds.alpha.ml.node2vec)
    private fun createEmbeddingsForAirports() {
        if (neo4jClient.query("call gds.graph.exists('Neo4jDemoGraph')").fetch().one().getOrNull()?.get("exists") == true) return
        // Create graph projection
        neo4jClient.query(
            """
        CALL gds.graph.project(
          'Neo4jDemoGraph',
          { Airport: { label: 'Airport' } },
          { HAS_FLIGHTS: { type: 'HAS_FLIGHTS', orientation: 'UNDIRECTED' } },
          {}
        );
        """
        ).run()
        // Compute node embeddings with node2vec
        neo4jClient.query(
            """
        CALL gds.node2vec.write('Neo4jDemoGraph', {
          embeddingDimension: 50, 
          iterations: 10, 
          writeProperty: 'node2vecEmbedding'
        });
        """
        ).run()
    }

    /**
     * Retrieves the Visualisations - a schema representation of the nodes & relationships
     * Though it doesn't provide the field properties of the Node/Relationships
     */
    private fun retrieveVisualization(): Visualization =
        neo4jClient.query("CALL db.schema.visualization").fetchAs<Visualization>()
            .mappedBy { _, record ->
                val nodes = record.get("nodes").asList { it.asNode() }
                    .map { node -> VisualizationNode(node.labels(), node.asMap()) }
                val relationships = record.get("relationships").asList { it.asRelationship() }
                    .map { relationship -> VisualizationRelationship(relationship.type(), relationship.asMap()) }
                Visualization(nodes, relationships)
            }.one() ?: throw RuntimeException("Visualization not found")

    companion object {
        private val FINAL_ANSWER_PATTERN: Regex = Regex("Final Answer: (.+)")
    }
}
