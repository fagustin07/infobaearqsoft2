package ar.edu.unq.weather.metric.infra.adapters

import ar.edu.unq.weather.metric.domain.*
import ar.edu.unq.weather.metric.domain.Unit
import ar.edu.unq.weather.metric.domain.exceptions.*
import io.github.resilience4j.retry.annotation.Retry
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import java.time.temporal.ChronoUnit
import kotlin.Array
import kotlin.String

@Service
class SyncHttpLoaderService : ILoaderService {
    @Autowired
    lateinit var restTemplate: RestTemplate

    private val log: Logger = LoggerFactory.getLogger(SyncHttpLoaderService::class.java)

    @Value("\${weather.loader.url}")
    private lateinit var baseURL: String

    @Retry(name = "loader-latest-retry")
    override fun currentWeather(locality: Locality, unit: Unit): Weather {
        val resEntity: ResponseEntity<WeatherDTO>
        val url = "$baseURL/latest?location=${locality.toValue()}"
        this.log.info("[GET] $url")
        try {
            resEntity = restTemplate.getForEntity(url, WeatherDTO::class.java)
        } catch (e: HttpStatusCodeException) {
            when (e.statusCode) {
                HttpStatus.BAD_REQUEST -> {
                    log.warn("[GET FAIL] BAD REQUEST: $url, ${e.responseBodyAsString}")
                    throw InfoBaeBadRequestError(e.responseBodyAsString)
                }

                HttpStatus.TOO_MANY_REQUESTS -> {
                    log.warn("[GET FAIL] TOO MANY REQUESTS: $url")
                    throw InfoBaeTooManyRequest()
                }

                HttpStatus.NOT_FOUND -> {
                    log.warn("[GET FAIL] NOT FOUND: $url")
                    throw LocalityNotFound(locality.toValue())
                }

                HttpStatus.INTERNAL_SERVER_ERROR -> {
                    log.error("[GET FAIL] status code ${e.statusCode}: $url")
                    throw InfoBaeInternalServerError(e.responseBodyAsString)
                }
                else -> {
                    log.error("[GET FAIL] ${e.cause}")
                    throw e
                }
            }
        }

        val weather = resEntity.body!!
        log.info("[GET OK] $url, $weather")
        return weather.format(unit, locality)
    }

    @Retry(name = "loader-list-retry")
    override fun weathersBetween(locality: Locality, unit: Unit, period: Period): List<Weather> {
        val resEntity: ResponseEntity<Array<WeatherDTO>>
        val url = "$baseURL/by_period"
        try {
            this.log.info("[POST] $url. locality {}, start {}, end {}", period.location, period.startDate, period.endDate)
            resEntity = restTemplate.postForEntity(url, period, Array<WeatherDTO>::class.java)
        } catch (e: HttpStatusCodeException) {
            when (e.statusCode) {
                HttpStatus.BAD_REQUEST -> {
                    log.warn("[GET FAIL] BAD REQUEST: $url, ${e.responseBodyAsString}")
                    throw InfoBaeBadRequestError(e.responseBodyAsString)
                }

                HttpStatus.TOO_MANY_REQUESTS -> {
                    log.warn("[GET FAIL] TOO MANY REQUESTS: $url")
                    throw InfoBaeTooManyRequest()
                }

                HttpStatus.NOT_FOUND -> {
                    log.warn("[GET FAIL] NOT FOUND: $url")
                    throw LocalityNotFound(locality.toValue())
                }

                else -> {
                    log.error("[GET FAIL] status code ${e.statusCode}: $url")
                    throw InfoBaeInternalServerError(e.responseBodyAsString)
                }
            }
        }

        val weatherList = resEntity.body!!
        log.info("[POST OK] Weathers list size ${weatherList.size}")
        return weatherList.map {
            Weather(
                    temperature = it.temperature,
                    unit = Unit.FAHRENHEIT,
                    sensation = it.sensation,
                    humidity = it.humidity,
                    locality = locality,
                    date = it.date.minus(3, ChronoUnit.HOURS)
            )
        }
    }
}
