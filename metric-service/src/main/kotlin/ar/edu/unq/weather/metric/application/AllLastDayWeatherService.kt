package ar.edu.unq.weather.metric.application

import ar.edu.unq.weather.metric.domain.*
import ar.edu.unq.weather.metric.domain.Unit
import ar.edu.unq.weather.metric.domain.exceptions.WeatherNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AllLastDayWeatherService {

    @Autowired
    lateinit var loaderService: ILoaderService

    fun execute(locality: Locality, unit: Unit, period: Period? = null): List<Weather> {
        val now = LocalDateTime.now()
        val yesterdayStart = now.minusDays(1)
        val currPeriod = period ?: Period(locality.toValue(), yesterdayStart, now)
        val weathers = loaderService.weathersBetween(locality, unit, currPeriod)

        if (weathers.isEmpty()) throw WeatherNotFoundException("Actualmente no poseemos informacion del dia solicitado")


        return weathers.sortedByDescending { it.date }
    }
}

