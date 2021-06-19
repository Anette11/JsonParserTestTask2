package com.example.jsonparsertesttask2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class SharedViewModel : ViewModel() {
    private var mutableLiveDataListOfHotels: MutableLiveData<List<Hotel>> = MutableLiveData()
    private var mutableLiveDataCustomActionBarTitle: MutableLiveData<String> = MutableLiveData()
    private var mutableLiveDataShowHotelInDetailInfo: MutableLiveData<Hotel> = MutableLiveData()
    private var mutableLiveDataSpinnerPosition: MutableLiveData<Int> = MutableLiveData()
    private var mutableLiveDataIsSpinnerVisible: MutableLiveData<Boolean> = MutableLiveData()

    fun getMutableLiveDataListOfHotels(): LiveData<List<Hotel>> = mutableLiveDataListOfHotels
    fun getMutableLiveDataSpinnerPosition(): LiveData<Int> = mutableLiveDataSpinnerPosition
    fun getMutableLiveDataIsSpinnerVisible(): LiveData<Boolean> = mutableLiveDataIsSpinnerVisible

    fun getMutableLiveDataCustomActionBarTitle(): LiveData<String> =
        mutableLiveDataCustomActionBarTitle

    fun getMutableLiveDataShowHotelInDetailInfo(): LiveData<Hotel> =
        mutableLiveDataShowHotelInDetailInfo

    fun setMutableLiveDataCustomActionBarTitle(title: String) =
        mutableLiveDataCustomActionBarTitle.postValue(title)

    fun setMutableLiveDataSpinnerPosition(position: Int) =
        mutableLiveDataSpinnerPosition.postValue(position)

    fun setMutableLiveDataShowHotelInDetailInfo(hotel: Hotel) =
        mutableLiveDataShowHotelInDetailInfo.postValue(hotel)

    fun setMutableLiveDataIsSpinnerVisible(isVisible: Boolean) =
        mutableLiveDataIsSpinnerVisible.postValue(isVisible)

    private suspend fun makeFirstRequestToGetAllHotelsCommonInfo(): List<Hotel>? {
        var list: List<Hotel>? = null
        val apiRequest = ApiRequestInstance
            .getApiRequestInstance().create(ApiRequestInterface::class.java)
        val responseListOfHotels = apiRequest.getListOfHotels().awaitResponse()

        if (responseListOfHotels.isSuccessful && responseListOfHotels.body() != null) {
            list = responseListOfHotels.body()
        }
        return list
    }

    private suspend fun makeSecondRequestToGetEachHotelDetailInfo(resultFirst: List<Hotel>) {
        val list: ArrayList<Hotel> = ArrayList()

        val apiRequest = ApiRequestInstance
            .getApiRequestInstance().create(ApiRequestInterface::class.java)

        for (i in 0 until resultFirst.count()) {
            val responseOneHotelDetailInfo = apiRequest
                .getHotelDetailInfo(resultFirst[i].id).awaitResponse()

            if (responseOneHotelDetailInfo.isSuccessful && responseOneHotelDetailInfo.body() != null) {
                list.add(responseOneHotelDetailInfo.body()!!)

                list[i].image = StringBuilder()
                    .append(ApiRequestConstants.API_REQUEST_IMAGE)
                    .append(responseOneHotelDetailInfo.body()!!.image)
                    .toString()
            }
        }
        mutableLiveDataListOfHotels.postValue(list)
    }

    fun makeApiRequest() {
        viewModelScope.launch(Dispatchers.IO) {
            val resultFirst = makeFirstRequestToGetAllHotelsCommonInfo()
            if (resultFirst != null) {
                makeSecondRequestToGetEachHotelDetailInfo(resultFirst)
            } else {
                mutableLiveDataListOfHotels.postValue(null)
            }
        }
    }
}