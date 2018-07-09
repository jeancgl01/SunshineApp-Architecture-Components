/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mtw.juancarlos.sunshineapp.ui.list

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar

import com.mtw.juancarlos.sunshineapp.R
import com.mtw.juancarlos.sunshineapp.ui.detail.DetailActivity
import kotlinx.android.synthetic.main.activity_forecast.*

import java.util.Date


/**
 * Displays a list of the next 14 days of forecasts
 */
class MainActivity : AppCompatActivity(), ForecastAdapter.ForecastAdapterOnItemClickHandler {

    private var mForecastAdapter: ForecastAdapter? = null
    private val mPosition = RecyclerView.NO_POSITION

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)


        /*
         * The ProgressBar that will indicate to the user that we are loading data. It will be
         * hidden when no data is loading.
         *
         * Please note: This so called "ProgressBar" isn't a bar by default. It is more of a
         * circle. We didn't make the rules (or the names of Views), we just follow them.
         */

        /*
         * A LinearLayoutManager is responsible for measuring and positioning item views within a
         * RecyclerView into a linear list. This means that it can produce either a horizontal or
         * vertical list depending on which parameter you pass in to the LinearLayoutManager
         * constructor. In our case, we want a vertical list, so we pass in the constant from the
         * LinearLayoutManager class for vertical lists, LinearLayoutManager.VERTICAL.
         *
         * There are other LayoutManagers available to display your data in uniform grids,
         * staggered grids, and more! See the developer documentation for more details.
         *
         * The third parameter (shouldReverseLayout) should be true if you want to reverse your
         * layout. Generally, this is only true with horizontal lists that need to support a
         * right-to-left layout.
         */
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        /* setLayoutManager associates the LayoutManager we created above with our RecyclerView */
        recyclerview_forecast.layoutManager = layoutManager

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        recyclerview_forecast.setHasFixedSize(true)

        /*
         * The ForecastAdapter is responsible for linking our weather data with the Views that
         * will end up displaying our weather data.
         *
         * Although passing in "this" twice may seem strange, it is actually a sign of separation
         * of concerns, which is best programming practice. The ForecastAdapter requires an
         * Android Context (which all Activities are) as well as an onClickHandler. Since our
         * MainActivity implements the ForecastAdapter ForecastOnClickHandler interface, "this"
         * is also an instance of that type of handler.
         */
        mForecastAdapter = ForecastAdapter(this, this)

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        recyclerview_forecast.adapter = mForecastAdapter
        showLoading()

    }

    /**
     * This method is for responding to clicks from our list.
     *
     * @param date Date of forecast
     */
    override fun onItemClick(date: Date) {
        val weatherDetailIntent = Intent(this@MainActivity, DetailActivity::class.java)
        val timestamp = date.time
        weatherDetailIntent.putExtra(DetailActivity.WEATHER_ID_EXTRA, timestamp)
        startActivity(weatherDetailIntent)
    }

    /**
     * This method will make the View for the weather data visible and hide the error message and
     * loading indicator.
     *
     *
     * Since it is okay to redundantly set the visibility of a View, we don't need to check whether
     * each view is currently visible or invisible.
     */
    private fun showWeatherDataView() {
        // First, hide the loading indicator
        pb_loading_indicator.visibility = View.INVISIBLE
        // Finally, make sure the weather data is visible
        recyclerview_forecast.visibility = View.VISIBLE
    }

    /**
     * This method will make the loading indicator visible and hide the weather View and error
     * message.
     *
     *
     * Since it is okay to redundantly set the visibility of a View, we don't need to check whether
     * each view is currently visible or invisible.
     */
    private fun showLoading() {
        // Then, hide the weather data
        recyclerview_forecast.visibility = View.INVISIBLE
        // Finally, show the loading indicator
        pb_loading_indicator.visibility = View.VISIBLE
    }
}
