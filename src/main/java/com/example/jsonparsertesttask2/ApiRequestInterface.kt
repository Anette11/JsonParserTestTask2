package com.example.jsonparsertesttask2

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiRequestInterface {
    @GET(ApiRequestConstants.API_REQUEST_GET_LIST_OF_HOTELS)
    fun getListOfHotels(): Call<List<Hotel>>

    @GET(ApiRequestConstants.API_REQUEST_GET_HOTEL_DETAIL_INFO)
    fun getHotelDetailInfo(@Path(ApiRequestConstants.API_REQUEST_ID) id: String): Call<Hotel>
}