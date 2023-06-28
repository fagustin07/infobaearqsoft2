package ar.edu.unq.weather.metric.domain

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

data class Weather(
        val temperature: Float,
        val unit: Unit,
        val sensation: Float,
        val humidity: Float,
        val locality: Locality,
        val date: LocalDateTime
) {
    fun format(unit: Unit): Weather {
        return Weather(
                temperature = parseTemp(this.temperature, unit),
                unit = unit,
                sensation = parseTemp(this.sensation, unit),
                humidity = this.humidity,
                locality = this.locality,
                date = date.minus(3, ChronoUnit.HOURS)
        )
    }

    private fun parseTemp(temperature: Float, unit: Unit): Float {
        return when (unit) {
            Unit.CELSIUS -> temperature - 273.15f
            Unit.KELVIN -> 1.8f *(temperature - 273.15f) + 32f
            else -> temperature
        }
    }
}

class WeatherDTO(
        val date: LocalDateTime,
        val temperature: Float,
        val sensation: Float,
        val humidity: Float
) {
    fun format(unit: Unit, locality: Locality): Weather {
        return Weather(
                temperature = parseTemp(this.temperature, unit),
                unit = unit,
                sensation = parseTemp(this.sensation, unit),
                humidity = humidity,
                locality = locality,
                date = date.minus(3, ChronoUnit.HOURS)
        )
    }

    private fun parseTemp(temperature: Float, unit: Unit): Float {
        return when (unit) {
            Unit.CELSIUS -> temperature - 273.15f
            Unit.KELVIN -> 1.8f *(temperature - 273.15f) + 32f
            else -> temperature
        }
    }
}

data class Period(
        val location: String,
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
        val startDate: LocalDateTime,
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
        val endDate: LocalDateTime
)

interface ILoaderService {
    fun currentWeather(locality: Locality, unit: Unit): Weather
    fun weathersBetween(locality: Locality, unit: Unit, period: Period): List<Weather>

}