package ar.edu.unq.weather.metric.application

import ar.edu.unq.weather.metric.domain.Unit
import ar.edu.unq.weather.metric.domain.Weather
import org.springframework.stereotype.Service
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Service
class CurrentWeather(
        url: String = "http://localhost:8080/api/v1/current"
) {

    private val log: Logger = LoggerFactory.getLogger(CurrentWeather::class.java)

    fun execute(): Weather {
        this.log.info("attaching new weather {}", Weather(50, Unit.CELSIUS))
        return Weather(50, Unit.CELSIUS)
    }
}
