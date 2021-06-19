package com.example.jsonparsertesttask2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.jsonparsertesttask2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        initialize()
    }

    private fun initialize() {
        setSharedViewModelInstance()
        makeApiRequestWithSharedViewModel()
        setCustomActionBar()
        setSpinnerVisibilityAndOnItemSelected()
        setAppropriateFragmentLayoutIntoMainActivityFragmentContainerWhenScreenRotates()
        setCustomActionBarTitle()
    }

    private fun setSharedViewModelInstance() {
        sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
    }

    private fun setCustomActionBar() {
        this.supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(R.layout.custom_action_bar)
    }

    private fun setAppropriateFragmentLayoutIntoMainActivityFragmentContainerWhenScreenRotates() {
        if (sharedViewModel.getMutableLiveDataCustomActionBarTitle().value == getString(R.string.arrow_back)) {
            showFragment(FragmentDetailInfo())
        } else {
            showFragment(FragmentRecyclerView())
        }
    }

    private fun setCustomActionBarTitle() {
        sharedViewModel.getMutableLiveDataCustomActionBarTitle().observe(this, {
            val customActionBarTitle = findViewById<TextView>(R.id.customActionBarTitle)
            if (it != null) {
                customActionBarTitle.text = it
            }
            if (it == getString(R.string.arrow_back)) {
                customActionBarTitle.setOnClickListener {
                    showFragment(FragmentRecyclerView())
                }
            }
        })
    }

    private fun makeApiRequestWithSharedViewModel() {
        if (sharedViewModel.getMutableLiveDataListOfHotels().value == null) {
            sharedViewModel.makeApiRequest()
        }
    }

    private fun setSpinnerVisibilityAndOnItemSelected() {
        val appCompatSpinner: AppCompatSpinner = findViewById(R.id.appCompatSpinner)

        sharedViewModel.getMutableLiveDataIsSpinnerVisible().observe(this, {
            if (it) {
                appCompatSpinner.visibility = View.VISIBLE
            } else {
                appCompatSpinner.visibility = View.GONE
            }
        })

        appCompatSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    sharedViewModel.setMutableLiveDataSpinnerPosition(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            replace(R.id.fragment_container, fragment)
            commit()
        }
    }
}