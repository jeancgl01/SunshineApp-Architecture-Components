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

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.mtw.juancarlos.sunshineapp.R
import com.mtw.juancarlos.sunshineapp.data.database.WeatherEntry
import com.mtw.juancarlos.sunshineapp.utilities.SunshineDateUtils
import com.mtw.juancarlos.sunshineapp.utilities.SunshineWeatherUtils
import kotlinx.android.synthetic.main.forecast_list_item.view.*

import java.util.Date

/**
 * Exposes a list of weather forecasts from a list of [WeatherEntry] to a [RecyclerView].
 */
class ForecastAdapter
/**
 * Creates a ForecastAdapter.
 *
 * @param context      Used to talk to the UI and app resources
 * @param clickHandler The on-click handler for this adapter. This single handler is called
 * when an item is clicked.
 */
(// The context we use to utility methods, app resources and layout inflaters
        private val mContext: Context, /*
     * Below, we've defined an interface to handle clicks on items within this Adapter. In the
     * constructor of our ForecastAdapter, we receive an instance of a class that has implemented
     * said interface. We store that instance in this variable to call the onItemClick method whenever
     * an item is clicked in the list.
     */
        private val mClickHandler: ForecastAdapterOnItemClickHandler) : RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder>() {
    /*
     * Flag to determine if we want to use a separate view for the list item that represents
     * today. This flag will be true when the phone is in portrait mode and false when the phone
     * is in landscape. This flag will be set in the constructor of the adapter by accessing
     * boolean resources.
     */
    private val mUseTodayLayout: Boolean = mContext.resources.getBoolean(R.bool.use_today_layout)
    private var mForecast: List<WeatherEntry>? = null


    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (like ours does) you
     * can use this viewType integer to provide a different layout. See
     * [android.support.v7.widget.RecyclerView.Adapter.getItemViewType]
     * for more details.
     * @return A new ForecastAdapterViewHolder that holds the View for each list item
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ForecastAdapterViewHolder {

        val layoutId = getLayoutIdByType(viewType)
        val view = LayoutInflater.from(mContext).inflate(layoutId, viewGroup, false)
        view.isFocusable = true
        return ForecastAdapterViewHolder(view)
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the weather
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param forecastAdapterViewHolder The ViewHolder which should be updated to represent the
     * contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(forecastAdapterViewHolder: ForecastAdapterViewHolder, position: Int) {
        val currentWeather = mForecast!![position]

        forecastAdapterViewHolder.bind(currentWeather,mContext,position)
    }

    /**
     * Converts the weather icon id from Open Weather to the local image resource id. Returns the
     * correct image based on whether the forecast is for today(large image) or the future(small image).
     *
     * @param weatherIconId Open Weather icon id
     * @param position      Position in list
     * @return Drawable image resource id for weather
     */
    private fun getImageResourceId(weatherIconId: Int, position: Int): Int {
        val viewType = getItemViewType(position)

        when (viewType) {

            VIEW_TYPE_TODAY -> return SunshineWeatherUtils
                    .getLargeArtResourceIdForWeatherCondition(weatherIconId)

            VIEW_TYPE_FUTURE_DAY -> return SunshineWeatherUtils
                    .getSmallArtResourceIdForWeatherCondition(weatherIconId)

            else -> throw IllegalArgumentException("Invalid view type, value of $viewType")
        }
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    override fun getItemCount(): Int {
        return if (null == mForecast) 0 else mForecast!!.size
    }

    /**
     * Returns an integer code related to the type of View we want the ViewHolder to be at a given
     * position. This method is useful when we want to use different layouts for different items
     * depending on their position. In Sunshine, we take advantage of this method to provide a
     * different layout for the "today" layout. The "today" layout is only shown in portrait mode
     * with the first item in the list.
     *
     * @param position index within our RecyclerView and list
     * @return the view type (today or future day)
     */
    override fun getItemViewType(position: Int): Int {
        return if (mUseTodayLayout && position == 0) {
            VIEW_TYPE_TODAY
        } else {
            VIEW_TYPE_FUTURE_DAY
        }
    }

    /**
     * Swaps the list used by the ForecastAdapter for its weather data. This method is called by
     * [MainActivity] after a load has finished. When this method is called, we assume we have
     * a new set of data, so we call notifyDataSetChanged to tell the RecyclerView to update.
     *
     * @param newForecast the new list of forecasts to use as ForecastAdapter's data source
     */
    fun swapForecast(newForecast: List<WeatherEntry>) {
        mForecast = newForecast
        notifyDataSetChanged()
    }

    /**
     * Returns the the layout id depending on whether the list item is a normal item or the larger
     * "today" list item.
     *
     * @param viewType
     * @return
     */
    private fun getLayoutIdByType(viewType: Int): Int {
        when (viewType) {

            VIEW_TYPE_TODAY -> {
                return R.layout.list_item_forecast_today
            }

            VIEW_TYPE_FUTURE_DAY -> {
                return R.layout.forecast_list_item
            }

            else -> throw IllegalArgumentException("Invalid view type, value of $viewType")
        }
    }

    /**
     * The interface that receives onItemClick messages.
     */
    interface ForecastAdapterOnItemClickHandler {
        fun onItemClick(date: Date)
    }

    /**
     * A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
     * a cache of the child views for a forecast item. It's also a convenient place to set an
     * OnClickListener, since it has access to the adapter and the views.
     */
    inner class ForecastAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        fun bind (currentWeather:WeatherEntry,mContext: Context, position:Int){
            /****************
             * Weather Icon *
             */
            val weatherIconId = currentWeather.weatherIconId
            val weatherImageResourceId = getImageResourceId(weatherIconId, position)
            itemView.weather_icon.setImageResource(weatherImageResourceId)

            /****************
             * Weather Date *
             */
            val dateInMillis = currentWeather.date.time
            /* Get human readable string using our utility method */
            val dateString = SunshineDateUtils.getFriendlyDateString(mContext, dateInMillis, false)

            /* Display friendly date string */
            itemView.date.text = dateString

            /***********************
             * Weather Description *
             */
            val description = SunshineWeatherUtils.getStringForWeatherCondition(mContext, weatherIconId)
            /* Create the accessibility (a11y) String from the weather description */
            val descriptionA11y = mContext.getString(R.string.a11y_forecast, description)

            /* Set the text and content description (for accessibility purposes) */
            itemView.weather_description.text = description
            itemView.weather_description.contentDescription = descriptionA11y

            /**************************
             * High (max) temperature *
             */
            val highInCelsius = currentWeather.max
            /*
              * If the user's preference for weather is fahrenheit, formatTemperature will convert
              * the temperature. This method will also append either °C or °F to the temperature
              * String.
              */
            val highString = SunshineWeatherUtils.formatTemperature(mContext, highInCelsius)
            /* Create the accessibility (a11y) String from the weather description */
            val highA11y = mContext.getString(R.string.a11y_high_temp, highString)

            /* Set the text and content description (for accessibility purposes) */
            itemView.high_temperature.text = highString
            itemView.high_temperature.contentDescription = highA11y

            /*************************
             * Low (min) temperature *
             */
            val lowInCelsius = currentWeather.min
            /*
              * If the user's preference for weather is fahrenheit, formatTemperature will convert
              * the temperature. This method will also append either °C or °F to the temperature
              * String.
              */
            val lowString = SunshineWeatherUtils.formatTemperature(mContext, lowInCelsius)
            val lowA11y = mContext.getString(R.string.a11y_low_temp, lowString)

            /* Set the text and content description (for accessibility purposes) */
            itemView.low_temperature.text = lowString
            itemView.low_temperature.contentDescription = lowA11y

            itemView.setOnClickListener(this)
        }

        /**
         * This gets called by the child views during a click. We fetch the date that has been
         * selected, and then call the onItemClick handler registered with this adapter, passing that
         * date.
         *
         * @param v the View that was clicked
         */
        override fun onClick(v: View) {
            val aPosition = adapterPosition
            val date = mForecast!![aPosition].date
            mClickHandler.onItemClick(date)
        }
    }

    companion object {

        private val VIEW_TYPE_TODAY = 0
        private val VIEW_TYPE_FUTURE_DAY = 1
    }
}