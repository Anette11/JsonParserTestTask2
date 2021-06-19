package com.example.jsonparsertesttask2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jsonparsertesttask2.databinding.FragmentRecyclerViewBinding

class FragmentRecyclerView : Fragment() {
    private var fragmentRecyclerViewBinding: FragmentRecyclerViewBinding? = null
    private val binding get() = fragmentRecyclerViewBinding!!
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var list: ArrayList<Hotel>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentRecyclerViewBinding =
            FragmentRecyclerViewBinding.inflate(inflater, container, false)
        sharedViewModel =
            ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        fragmentRecyclerViewBinding!!.circularProgressIndicator.visibility = View.VISIBLE
        sharedViewModel.setMutableLiveDataIsSpinnerVisible(true)
        sharedViewModel.setMutableLiveDataCustomActionBarTitle(resources.getString(R.string.app_name))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshRecycleViewList()
    }

    private fun refreshRecycleViewList() {
        sharedViewModel.getMutableLiveDataSpinnerPosition().observe(viewLifecycleOwner, { it ->
            when (it) {
                1 -> {
                    sharedViewModel.getMutableLiveDataListOfHotels().observe(viewLifecycleOwner, {
                        list =
                            sharedViewModel.getMutableLiveDataListOfHotels().value as ArrayList<Hotel>
                    })
                    val sortedListDistance = list.toList().sortedBy { it.distance.toDouble() }
                        .toMutableList() as ArrayList<Hotel>
                    setRecyclerView(sortedListDistance)
                }
                2 -> {
                    sharedViewModel.getMutableLiveDataListOfHotels().observe(viewLifecycleOwner, {
                        list =
                            sharedViewModel.getMutableLiveDataListOfHotels().value as ArrayList<Hotel>
                    })
                    val sortedListRooms = list.toList().sortedBy {
                        it.suitesAvailability.replace(
                            HotelConstants.HOTEL_CONSTANTS_COLON,
                            HotelConstants.HOTEL_CONSTANTS_SPACE
                        ).trim().toMutableList().size
                    }
                        .toMutableList() as ArrayList<Hotel>
                    setRecyclerView(sortedListRooms)
                }
                else -> {
                    sharedViewModel.getMutableLiveDataListOfHotels().observe(viewLifecycleOwner, {
                        if (sharedViewModel.getMutableLiveDataListOfHotels().value == null) {
                            sharedViewModel.makeApiRequest()
                        }
                        list =
                            sharedViewModel.getMutableLiveDataListOfHotels().value as ArrayList<Hotel>
                        setRecyclerView(list)
                    })
                }
            }
        })
    }

    private fun setRecyclerView(arrayList: ArrayList<Hotel>) {
        with(binding.RecyclerView) {
            layoutManager = LinearLayoutManager(context)

            adapter = HotelRecyclerViewAdapter(arrayList) {
                sharedViewModel.setMutableLiveDataShowHotelInDetailInfo(it)
                showFragment(FragmentDetailInfo())
            }
        }
        fragmentRecyclerViewBinding!!.circularProgressIndicator.visibility = View.GONE
    }

    private fun showFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction().apply {
            setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            replace(R.id.fragment_container, fragment)
            commit()
        }
    }

    override fun onDestroyView() {
        fragmentRecyclerViewBinding = null
        super.onDestroyView()
    }
}