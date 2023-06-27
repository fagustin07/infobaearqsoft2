package ar.edu.unq.weather.metric.infra.adapters

import ar.edu.unq.weather.metric.domain.ILoaderService
import ar.edu.unq.weather.metric.domain.Locality
import ar.edu.unq.weather.metric.domain.Unit
import ar.edu.unq.weather.metric.domain.Weather
import ar.edu.unq.weather.metric.domain.exceptions.ConnRefException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import java.lang.RuntimeException
import java.time.LocalDateTime


@Service
class SyncHttpLoaderService: ILoaderService {
    @Autowired
    lateinit var restTemplate: RestTemplate

    private val log: Logger = LoggerFactory.getLogger(SyncHttpLoaderService::class.java)

    val baseURL = "http://localhost:8080/api/v1/weather"
    override fun currentWeather(locality: Locality, unit: Unit): Weather {
        val resEntity: ResponseEntity<WeatherDTO>
        try {
            val url = "$baseURL/latest?location=${locality.toValue()}"
            this.log.info("[FETCH] $url")
            resEntity = restTemplate.getForEntity(url, WeatherDTO::class.java)
        } catch (err: RestClientException) {
            this.log.error("[FETCH FAIL] Loader-Service Connection refused")
            throw ConnRefException("Loader-Service")
        }

        return when (resEntity.statusCode) {
            HttpStatus.OK -> {
                val weather = resEntity.body!!
                log.info("Fetch successfully {}", weather)
                weather.format(unit,locality)
            }
            else -> {
                log.warn("Fetch failed with status ${resEntity.statusCode}")
                throw RuntimeException("Not available")
            }
        }
    }

    override fun avgLastWeek(locality: Locality, unit: Unit): Weather {
        TODO("Not yet implemented")
    }

    override fun lastWeekTemps(locality: Locality, unit: Unit): List<Weather> {
        TODO("Not yet implemented")
    }

    override fun avgLastDay(locality: Locality, unit: Unit): Weather {
        TODO("Not yet implemented")
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
                sensation = sensation,
                humidity = humidity,
                locality = locality,
                date = date
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