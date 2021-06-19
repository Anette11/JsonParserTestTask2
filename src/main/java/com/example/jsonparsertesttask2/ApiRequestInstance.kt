package com.example.jsonparsertesttask2

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiRequestInstance {
    companion object {
        private const val BASE_URL = ApiRequestConstants.API_REQUEST_BASE_URL
        fun getApiRequestInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}