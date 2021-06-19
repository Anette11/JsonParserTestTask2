package com.example.jsonparsertesttask2

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.jsonparsertesttask2.databinding.OneItemForRecyclerViewBinding
import com.squareup.picasso.Picasso
import kotlin.math.roundToInt

class HotelRecyclerViewAdapter(
    private val listOfHotels: List<Hotel>,
    private val onItemHotelAdapterClickListener: (Hotel) -> Unit
) :
    RecyclerView.Adapter<HotelRecyclerViewAdapter.HotelViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelViewHolder {
        val itemBinding = OneItemForRecyclerViewBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return HotelViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: HotelViewHolder, position: Int) {
        val hotel: Hotel = listOfHotels[position]
        holder.bind(hotel)
        holder.itemView.findViewById<Button>(R.id.buttonLearnMore)
            .setOnClickListener { onItemHotelAdapterClickListener(hotel) }
    }

    override fun getItemCount(): Int = listOfHotels.size

    class HotelViewHolder(
        private val oneItemForRecyclerViewBinding: OneItemForRecyclerViewBinding
    ) :
        RecyclerView.ViewHolder(oneItemForRecyclerViewBinding.root) {

        fun bind(hotel: Hotel) = with(oneItemForRecyclerViewBinding) {
            textViewId.text = setTextInTextViewId(hotel.id)
            textViewHotelName.text = createHotelNameWithStars(hotel.name, hotel.stars)
            textViewAddress.text = hotel.address
            textViewDistance.text = setTextInTextViewDistance(hotel.distance)
            textSuitesAvailable.text = showVacantRoomsInfo(hotel.suitesAvailability)

            Picasso.get()
                .load(hotel.image)
                .error(R.drawable.ic_hotel)
                .placeholder(R.drawable.progress_indicator_animated)
                .transform(CroppedBitmapTransformation())
                .into(imageViewHotelPicture)
        }

        private fun setTextInTextViewId(id: String): String {
            return StringBuilder()
                .append(HotelConstants.HOTEL_CONSTANTS_HOTEL_ID)
                .append(HotelConstants.HOTEL_CONSTANTS_SPACE)
                .append(id)
                .toString()
        }

        private fun setTextInTextViewDistance(distance: String): String {
            return StringBuilder()
                .append(distance.toDouble().roundToInt().toString())
                .append(HotelConstants.HOTEL_CONSTANTS_SPACE)
                .append(HotelConstants.HOTEL_CONSTANTS_KM_FROM_CITY_CENTER)
                .toString()
        }

        private fun createHotelNameWithStars(name: String, stars: String): String {
            val stringBuilder: StringBuilder = StringBuilder()
                .append(name)
                .append(HotelConstants.HOTEL_CONSTANTS_SPACE)

            val starsInInt = stars.toDouble().toInt()

            for (i in 1..starsInInt) {
                stringBuilder.append(HotelConstants.HOTEL_CONSTANTS_STAR)
            }
            return stringBuilder.toString()
        }

        private fun showVacantRoomsInfo(suitesAvailable: String): String {
            var result = suitesAvailable
                .replace(
                    HotelConstants.HOTEL_CONSTANTS_COLON,
                    HotelConstants.HOTEL_CONSTANTS_COMMA.plus(HotelConstants.HOTEL_CONSTANTS_SPACE),
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
    }
}