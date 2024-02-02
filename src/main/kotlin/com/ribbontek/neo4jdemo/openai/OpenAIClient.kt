package com.ribbontek.neo4jdemo.openai

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(
    name = "openai-client",
    url = "https://api.openai.com",
    configuration = [OpenAIClientConfig::class]
)
interface OpenAIClient {
    @PostMapping(
        value = ["/v1/chat/completions"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getCompletion(@RequestBody request: CompletionRequest): CompletionResponse

    @PostMapping(
        value = ["/v1/embeddings"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getEmbeddings(@RequestBody input: EmbeddingsRequest): EmbeddingsResponse
}
