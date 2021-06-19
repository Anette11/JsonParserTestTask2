package com.example.jsonparsertesttask2

import com.google.gson.annotations.SerializedName

data class Hotel(
    @SerializedName(HotelConstants.HOTEL_CONSTANTS_ID)
    val id: String,

    @SerializedName(HotelConstants.HOTEL_CONSTANTS_NAME)
    val name: String,

    @SerializedName(HotelConstants.HOTEL_CONSTANTS_ADDRESS)
    val address: String,

    @SerializedName(HotelConstants.HOTEL_CONSTANTS_STARS)
    val stars: String,

    @SerializedName(HotelConstants.HOTEL_CONSTANTS_DISTANCE)
    val distance: String,

    @SerializedName(HotelConstants.HOTEL_CONSTANTS_SUITES_AVAILABILITY)
    val suitesAvailability: String,

    @SerializedName(HotelConstants.HOTEL_CONSTANTS_IMAGE)
    var image: String = HotelConstants.HOTEL_CONSTANTS_EMPTY,

    @SerializedName(HotelConstants.HOTEL_CONSTANTS_LAT)
    val lat: String,

    @SerializedName(HotelConstants.HOTEL_CONSTANTS_LON)
    val lon: String
)