package ar.edu.unq.weather.metric.application

import ar.edu.unq.weather.metric.domain.*
import ar.edu.unq.weather.metric.domain.Unit
import ar.edu.unq.weather.metric.domain.exceptions.LocalityNotFound
import ar.edu.unq.weather.metric.domain.exceptions.WeatherNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class AllWeathersLastWeekService {

    @Autowired
    lateinit var loaderService: ILoaderService

    fun execute(locality: Locality, unit: Unit, period: Period? = null): List<Weather> {
        val now = LocalDateTime.now()
        val oneWeekAgoStartDate = now.minusWeeks(1)
        val currPeriod = period ?: Period(locality.toValue(), oneWeekAgoStartDate, now)

        val weathers = loaderService.weathersBetween(locality, unit, currPeriod)
        if (weathers.isEmpty()) throw WeatherNotFoundException("Not registered data from ${locality.toValue()}")

        return weathers.sortedByDescending { it.date }
    }
}
