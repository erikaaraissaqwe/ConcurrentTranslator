package br.edu.ifsp.scl.concurrenttranslator.model.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DeepTranslateApiClient {
    private const val BASE_URL = "https://deep-translate1.p.rapidapi.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: DeepTranslateApiService = retrofit.create(DeepTranslateApiService::class.java)
}