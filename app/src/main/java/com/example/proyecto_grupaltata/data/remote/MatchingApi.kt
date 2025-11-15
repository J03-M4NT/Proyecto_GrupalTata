package com.example.proyecto_grupaltata.data.remote

import com.example.proyecto_grupaltata.domain.model.Candidate
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

data class MatchingRequest(val skills: List<String>)

interface MatchingApi {

    @POST("matches") // The endpoint for finding matches
    suspend fun findMatches(@Body request: MatchingRequest): Response<List<Candidate>>

}

object RetrofitClient {

    // We'll replace this with the real Firebase Cloud Function URL later
    private const val BASE_URL = "https://api.example.com/"

    val instance: MatchingApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(MatchingApi::class.java)
    }
}