package com.ribbontek.neo4jdemo.openai

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Completion Request used for communicating with OpenAI
 * https://platform.openai.com/docs/api-reference/chat/create
 * gpt-3.5-turbo
 */
@JsonInclude(NON_NULL)
data class CompletionRequest(
    val model: String = "gpt-3.5-turbo",
    val messages: List<CompletionMessage>,
    val temperature: Int? = null,
    @get:JsonProperty("top_p") val altTemperature: Int? = null,
    @get:JsonProperty("n") val totalChoices: Int? = null,
    val stream: Boolean? = null,
    val stop: List<String>? = null,
    @get:JsonProperty("max_tokens") val maxTokens: Int? = null,
    @get:JsonProperty("presence_penalty") val presencePenalty: Int? = null,
    @get:JsonProperty("frequency_penalty") val frequencyPenalty: Int? = null,
    @get:JsonProperty("logit_bias") val logitBias: Map<String, Any>? = null,
    val user: String? = null
)

@JsonInclude(NON_NULL)
data class CompletionMessage(
    val role: String = "user",
    val content: String,
    val name: String? = null
)

data class CompletionResponse(
    val id: String,
    @get:JsonProperty("object") val openAiObject: String,
    val created: Long,
    val model: String,
    val usage: ModelUsage,
    val choices: List<CompletionChoice>
)

data class ModelUsage(
    @get:JsonProperty("prompt_tokens") val promptTokens: Int? = null,
    @get:JsonProperty("completion_tokens") val completionTokens: Int? = null,
    @get:JsonProperty("total_tokens") val totalTokens: Int? = null
)

data class CompletionChoice(
    val message: ChoiceMessage,
    @get:JsonProperty("finish_reason") val finishedReason: String,
    val index: Int
)

data class ChoiceMessage(
    val role: String,
    val content: String
)
