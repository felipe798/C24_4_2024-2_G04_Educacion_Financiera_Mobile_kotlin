package com.principe.felipe.finango_d1.network

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

data class GPTRequest(
    val model: String,
    val messages: List<Map<String, String>>
)

data class GPTResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: MessageContent
)

data class MessageContent(
    val role: String,
    val content: String
)

interface GPTApiService {
    @Headers("Content-Type: application/json")
    @POST("chat/completions")
    suspend fun getGPTResponse(@Body request: GPTRequest): GPTResponse
}
