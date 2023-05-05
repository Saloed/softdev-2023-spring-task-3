package com.example.test.data

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface IChannelMessages {
    @GET("channels/{channelId}/messages")
    suspend fun messages(
        @Header("Authorization") auth: String,
        @Path("channelId") channelId: String
    ): String
}