///*
// * Copyright (C) 2017 The Android Open Source Project
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.mtw.juancarlos.sunshineapp.data
//
//import android.util.Log
//
//import com.mtw.juancarlos.sunshineapp.AppExecutors
//import com.mtw.juancarlos.sunshineapp.data.database.WeatherDao
//import com.mtw.juancarlos.sunshineapp.data.network.WeatherNetworkDataSource
//
///**
// * Handles data operations in Sunshine. Acts as a mediator between [WeatherNetworkDataSource]
// * and [WeatherDao]
// */
//class SunshineRepository private constructor(private val mWeatherDao: WeatherDao,
//                                             private val mWeatherNetworkDataSource: WeatherNetworkDataSource,
//                                             private val mExecutors: AppExecutors) {
//    private var mInitialized = false
//
//    /**
//     * Checks if there are enough days of future weather for the app to display all the needed data.
//     *
//     * @return Whether a fetch is needed
//     */
//    private fun isFetchNeeded(): Boolean{
//        // TODO Finish this method when instructed
//        return true
//    }
//    /**
//     * Creates periodic sync tasks and checks to see if an immediate sync is required. If an
//     * immediate sync is required, this method will take care of making sure that sync occurs.
//     */
//    @Synchronized
//    fun initializeData() {
//
//        // Only perform initialization once per app lifetime. If initialization has already been
//        // performed, we have nothing to do in this method.
//        if (mInitialized) return
//        mInitialized = true
//
//        // TODO Finish this method when instructed
//    }
//
//    /**
//     * Database related operations
//     */
//
//    /**
//     * Deletes old weather data because we don't need to keep multiple days' data
//     */
//    private fun deleteOldData() {
//        // TODO Finish this method when instructed
//    }
//
//    /**
//     * Network related operation
//     */
//
//    private fun startFetchWeatherService() {
//        // TODO Finish this method when instructed
//    }
//
//    companion object {
//        private val LOG_TAG = SunshineRepository::class.java.simpleName
//
//        // For Singleton instantiation
//        private val LOCK = Any()
//        private var sInstance: SunshineRepository? = null
//
//        @Synchronized
//        fun getInstance(
//                weatherDao: WeatherDao, weatherNetworkDataSource: WeatherNetworkDataSource,
//                executors: AppExecutors): SunshineRepository? {
//            Log.d(LOG_TAG, "Getting the repository")
//            if (sInstance == null) {
//                synchronized(LOCK) {
//                    sInstance = SunshineRepository(weatherDao, weatherNetworkDataSource,
//                            executors)
//                    Log.d(LOG_TAG, "Made new repository")
//                }
//            }
//            return sInstance
//        }
//    }
//
//}