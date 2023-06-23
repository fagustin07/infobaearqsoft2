package ar.edu.unq.weather.metric.application

import ar.edu.unq.weather.metric.domain.Unit
import ar.edu.unq.weather.metric.domain.Weather
import org.springframework.stereotype.Service

@Service
class CurrentWeather(
        url: String = "http://localhost:8080/api/v1/current"
) {

    fun execute(): Weather {
        return Weather(20, Unit.CELSIUS)
    }
}
