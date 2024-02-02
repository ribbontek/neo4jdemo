package com.ribbontek.neo4jdemo.openai

import feign.RequestInterceptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenAIClientConfig {

    @Value("\${com.ribbontek.openai.apikey}")
    private lateinit var openAIApiKey: String

    @Bean
    fun authRequestInterceptor(): RequestInterceptor {
        return RequestInterceptor {
            if (!it.headers().containsKey("Authorization")) {
                it.header("Authorization", "Bearer $openAIApiKey")
            }
        }
    }
}
