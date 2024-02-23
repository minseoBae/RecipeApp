package com.example.recipeapp

import okhttp3.Request
import okhttp3.OkHttpClient
import okio.IOException

object ApiGateway {
    private val client = OkHttpClient()

    fun getRecipeDataFromApi(url: String): String {
        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        val response = client.newCall(request).execute()

        if (response.isSuccessful) {
            return response.body?.string() ?: ""
        } else {
            throw IOException("Unexpected code ${response.code}")
        }
    }
}