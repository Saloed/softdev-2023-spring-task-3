package com.example.test.data

import retrofit2.http.GET
import retrofit2.http.Header

interface IChatList {
    @GET("users/@me/channels")
    suspend fun chats(@Header("Authorization") auth: String): String
}