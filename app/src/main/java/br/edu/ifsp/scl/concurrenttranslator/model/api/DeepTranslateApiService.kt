package br.edu.ifsp.scl.concurrenttranslator.model.api

import br.edu.ifsp.scl.concurrenttranslator.model.domain.Language
import br.edu.ifsp.scl.concurrenttranslator.model.domain.Languages
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface DeepTranslateApiService {
    @Headers(
        "x-rapidapi-host: deep-translate1.p.rapidapi.com",
        "x-rapidapi-key: 6acc2eb728mshf0b41abd4d4597ep1bf582jsn77e505dee1fa")
    @GET("language/translate/v2/languages")
    fun getLanguages(): Call<Languages>
}