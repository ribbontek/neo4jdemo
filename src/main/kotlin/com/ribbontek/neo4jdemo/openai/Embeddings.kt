package com.ribbontek.neo4jdemo.openai

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

/**
 * Embeddings request
 * info: https://platform.openai.com/docs/guides/embeddings/what-are-embeddings
 * api ref: https://platform.openai.com/docs/api-reference/embeddings/create
 * models: text-embedding-3-small, text-embedding-3-large
 */
@JsonInclude(NON_NULL)
data class EmbeddingsRequest(
    val input: String,
    val model: String,
    val dimensions: Int? = null,
    val user: String? = null
)

/**
 * Embeddings response
 */
data class EmbeddingsResponse(
    @JsonProperty("object")
    val objectType: String,
    val data: List<EmbeddingData>,
    val model: String,
    val usage: EmbeddingUsage
)

data class EmbeddingData(
    @JsonProperty("object")
    val objectType: String,
    val index: Int,
    val embedding: List<BigDecimal>
)

data class EmbeddingUsage(
    @JsonProperty("prompt_tokens")
    val promptTokens: Int,
    @JsonProperty("total_tokens")
    val totalTokens: Int
)
