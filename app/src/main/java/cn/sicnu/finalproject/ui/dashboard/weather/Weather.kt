package cn.sicnu.finalproject.ui.dashboard.weather

import cn.sicnu.finalproject.ui.dashboard.weather.CityInfo
import cn.sicnu.finalproject.ui.dashboard.weather.Data

data class Weather(
    val cityInfo: CityInfo,
    val `data`: Data,
    val date: String,
    val message: String,
    val status: Int,
    val time: String
)