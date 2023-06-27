package ar.edu.unq.weather.metric.domain

import java.time.LocalDateTime

data class Weather(
        val temperature: Float,
        val unit: Unit,
        val sensation: Float,
        val humidity: Float,
        val locality: Locality,
        val date: LocalDateTime
)

enum class Unit {
    FAHRENHEIT, CELSIUS, KELVIN;
}


interface ILoaderService {
    fun currentWeather(locality: Locality = Locality.QUILMES, unit: Unit = Unit.CELSIUS): Weather
    fun avgLastWeek(locality: Locality = Locality.QUILMES, unit: Unit= Unit.CELSIUS): Weather
    fun lastWeekTemps(locality: Locality = Locality.QUILMES, unit: Unit= Unit.CELSIUS): List<Weather>
    fun avgLastDay(locality: Locality = Locality.QUILMES, unit: Unit= Unit.CELSIUS): Weather

}