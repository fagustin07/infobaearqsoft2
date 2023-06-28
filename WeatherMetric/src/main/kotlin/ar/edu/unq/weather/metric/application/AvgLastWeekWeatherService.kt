package ar.edu.unq.weather.metric.application

import ar.edu.unq.weather.metric.domain.*
import ar.edu.unq.weather.metric.domain.Unit
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class AvgLastWeekWeatherService {

    @Autowired
    lateinit var loaderService: ILoaderService

    fun execute(locality: Locality, unit: Unit, period: Period? = null): Weather {
        val oneWeekAgoStartDate = LocalDate.now().atStartOfDay().minusWeeks(1)
        val yesterdayEndDate = oneWeekAgoStartDate.plusWeeks(1).minusNanos(1)
        val currPeriod = period ?: Period(locality.toValue(), oneWeekAgoStartDate, yesterdayEndDate)

        val weathers = loaderService.weathersBetween(locality, unit, currPeriod)

        val weatherAcc = weathers.reduce { total, curr ->
            Weather(date = total.date,
                    temperature = total.temperature + curr.temperature,
                    sensation = total.sensation + curr.sensation,
                    humidity = total.humidity + curr.humidity,
                    unit = unit,
                    locality = locality)
        }
        val size = weathers.size

        return Weather(
                date = weatherAcc.date,
                temperature = weatherAcc.temperature/ size,
                sensation = weatherAcc.sensation/ size,
                humidity = weatherAcc.humidity/ size,
                unit = Unit.CELSIUS,
                locality = locality)
                .format(unit)
    }
}