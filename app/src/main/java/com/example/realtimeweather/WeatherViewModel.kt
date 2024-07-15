package com.example.realtimeweather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realtimeweather.api.Constant
import com.example.realtimeweather.api.NetworkResponse
import com.example.realtimeweather.api.RetrofitInstance
import com.example.realtimeweather.api.WeatherModal
import kotlinx.coroutines.launch
import retrofit2.Retrofit


class WeatherViewModel : ViewModel() {

    private val weatherApi = RetrofitInstance.weatherApi
    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModal>>()
    val weatherResult: LiveData<NetworkResponse<WeatherModal>> = _weatherResult

    fun getData(city: String) {
        _weatherResult.value = NetworkResponse.Loading

        viewModelScope.launch {
            val response = weatherApi.getWeather(Constant.apiKey, city)
            try {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _weatherResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _weatherResult.value = NetworkResponse.Error("Failed to load data")
                }
            } catch (e: Exception) {
                _weatherResult.value = NetworkResponse.Error("Failed to load data")
            }

        }

    }
}