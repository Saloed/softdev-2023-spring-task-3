package com.example.test.data

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface IChannelMessages {
    @GET("channels/{channelId}/messages")
    suspend fun messages(
        @Header("Authorization") auth: String,
        @Path("channelId") channelId: String
    ): String

    @GET("users/@me")
    suspend fun me(@Header("Authorization") auth: String): String

    @POST("channels/{channelId}/messages")
    suspend fun sendMessage(
        @Header("Authorization") auth: String,
        @Path("channelId") chatId: String,
        @Body content: String,
        @Header("Content-Type") contentType: String = "application/json"
    )
}