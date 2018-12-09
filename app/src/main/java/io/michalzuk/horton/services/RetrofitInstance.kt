package io.michalzuk.horton.services

import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit


object RetrofitInstance {
    private var retrofit: Retrofit? = null
    private val BASE_URL = GlobalStorage.getDomain()
    fun getRetrofitInstance() : Retrofit? {
        if (retrofit == null && !BASE_URL.isEmpty()) {
            retrofit = retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
        return retrofit
    }
}