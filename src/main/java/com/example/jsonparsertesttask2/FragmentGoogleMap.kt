package com.example.jsonparsertesttask2

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jsonparsertesttask2.databinding.FragmentGoogleMapBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class FragmentGoogleMap() : Fragment() {
    private var fragmentGoogleMapBinding: FragmentGoogleMapBinding? = null
    private val binding get() = fragmentGoogleMapBinding!!

    private var hotelName: String = HotelConstants.HOTEL_CONSTANTS_HOTEL_NAME
    private var lat: Double = 0.0
    private var lon: Double = 0.0

    constructor(lat: String, lon: String, hotelName: String) : this() {
        this.lat = lat.toDouble()
        this.lon = lon.toDouble()
        this.hotelName = hotelName
    }

    private val callback = OnMapReadyCallback { googleMap ->
        val pointOnMap = LatLng(lat, lon)
        googleMap.addMarker(MarkerOptions().position(pointOnMap).title(hotelName))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pointOnMap, 15f))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentGoogleMapBinding = FragmentGoogleMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onDestroyView() {
        fragmentGoogleMapBinding = null
        super.onDestroyView()
    }
}