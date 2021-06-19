package com.example.jsonparsertesttask2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.jsonparsertesttask2.databinding.FragmentDetailInfoBinding
import com.squareup.picasso.Picasso
import kotlin.math.roundToInt

class FragmentDetailInfo : Fragment() {
    private var fragmentDetailInfoBinding: FragmentDetailInfoBinding? = null
    private val binding get() = fragmentDetailInfoBinding!!
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var hotel: Hotel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentDetailInfoBinding = FragmentDetailInfoBinding.inflate(inflater, container, false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        sharedViewModel.setMutableLiveDataIsSpinnerVisible(false)
        sharedViewModel.setMutableLiveDataCustomActionBarTitle(getString(R.string.arrow_back))
        fillContent()
        return binding.root
    }

    private fun fillContent() {
        sharedViewModel.getMutableLiveDataShowHotelInDetailInfo().observe(viewLifecycleOwner, {
            if (it != null) {
                hotel = it

                val fragmentMaps = FragmentGoogleMap(
                    hotel.lat,
                    hotel.lon,
                    createHotelNameWithStars(hotel.name, hotel.stars)
                )
                showFragmentGoogleMap(fragmentMaps)

                with(fragmentDetailInfoBinding) {
                    this!!.textViewHotelName.text =
                        createHotelNameWithStars(hotel.name, hotel.stars)

                    textViewAddress.text = StringBuilder()
                        .append(getString(R.string.address))
                        .append(HotelConstants.HOTEL_CONSTANTS_SPACE)
                        .append(hotel.address)
                        .toString()

                    textViewId.text = StringBuilder()
                        .append(HotelConstants.HOTEL_CONSTANTS_HOTEL_ID)
                        .append(HotelConstants.HOTEL_CONSTANTS_SPACE)
                        .append(hotel.id)
                        .toString()

                    textSuitesAvailable.text =
                        findOutTotalAmountOfVacantRooms(hotel.suitesAvailability)

                    textViewDistance.text = setTextInTextViewDistance(hotel.distance)

                    Picasso.get()
                        .load(hotel.image)
                        .error(R.drawable.ic_hotel)
                        .placeholder(R.drawable.progress_indicator_animated)
                        .transform(CroppedBitmapTransformation())
                        .into(imageViewHotelPicture)
                }
            }
        })
    }

    private fun setTextInTextViewDistance(distance: String): String {
        return StringBuilder()
            .append(getString(R.string.distance_from_city_center))
            .append(HotelConstants.HOTEL_CONSTANTS_SPACE)
            .append(distance.toDouble().roundToInt())
            .append(HotelConstants.HOTEL_CONSTANTS_SPACE)
            .append(getString(R.string.km))
            .toString()
    }

    private fun createHotelNameWithStars(name: String, stars: String): String {
        val stringBuilder: StringBuilder = StringBuilder()
            .append(name)
            .append(HotelConstants.HOTEL_CONSTANTS_SPACE)

        val starsToInt = stars.toDouble().toInt()

        for (i in 1..starsToInt) {
            stringBuilder.append(HotelConstants.HOTEL_CONSTANTS_STAR)
        }
        return stringBuilder.toString()
    }

    private fun findOutTotalAmountOfVacantRooms(suitesAvailable: String): String {
        var result = suitesAvailable
            .replace(
                HotelConstants.HOTEL_CONSTANTS_COLON,
                HotelConstants.HOTEL_CONSTANTS_COMMA
                    .plus(HotelConstants.HOTEL_CONSTANTS_SPACE),
                true
            )
            .trim()
            .replace(
                HotelConstants.HOTEL_CONSTANTS_SQUARE_BRACKET_LEFT,
                HotelConstants.HOTEL_CONSTANTS_EMPTY
            )
            .replace(
                HotelConstants.HOTEL_CONSTANTS_SQUARE_BRACKET_RIGHT,
                HotelConstants.HOTEL_CONSTANTS_EMPTY
            )

        if (result.last() == ',') {
            result = result.substring(0, result.length - 1)
        }
        return StringBuilder()
            .append(HotelConstants.HOTEL_CONSTANTS_VACANT_ROOMS)
            .append(HotelConstants.HOTEL_CONSTANTS_SPACE)
            .append(result)
            .toString()
    }

    override fun onDestroyView() {
        fragmentDetailInfoBinding = null
        super.onDestroyView()
    }

    private fun showFragmentGoogleMap(fragment: Fragment) {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentShowHotelOnMap, fragment)
            commit()
        }
    }
}