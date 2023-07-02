package ar.edu.unq.weather.metric.application

import ar.edu.unq.weather.metric.domain.*
import ar.edu.unq.weather.metric.domain.Unit
import ar.edu.unq.weather.metric.domain.exceptions.WeatherNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class AvgLastDayWeatherService {

    @Autowired
    lateinit var loaderService: ILoaderService

    fun execute(locality: Locality, unit: Unit, period: Period? = null): Weather {
        val now = LocalDateTime.now()
        val yesterdayStart = now.minusDays(1)
        val currPeriod = period ?: Period(locality.toValue(), yesterdayStart, now)

        val weathers = loaderService.weathersBetween(locality, unit, currPeriod).sortedByDescending { it.date }

        if (weathers.isEmpty()) throw WeatherNotFoundException("Not registered data from ${locality.toValue()}")
        val weatherAcc = weathers.reduce { total, curr ->
            Weather(date = total.date,
                    temperature = total.temperature + curr.temperature,
                    sensation = total.sensation + curr.sensation,
                    humidity = total.humidity + curr.humidity,
                    unit = total.unit,
                    locality = locality)
        }
        val size = weathers.size

        return Weather(
                date = weatherAcc.date,
                temperature = weatherAcc.temperature/ size,
                sensation = weatherAcc.sensation/ size,
                humidity = weatherAcc.humidity/ size,
                unit = weatherAcc.unit,
                locality = locality)
                .format(unit)
    }
}