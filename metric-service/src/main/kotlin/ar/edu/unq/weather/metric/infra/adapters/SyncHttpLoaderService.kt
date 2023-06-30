package ar.edu.unq.weather.metric.infra.adapters

import ar.edu.unq.weather.metric.domain.*
import ar.edu.unq.weather.metric.domain.Unit
import ar.edu.unq.weather.metric.domain.exceptions.ConnRefException
import ar.edu.unq.weather.metric.domain.exceptions.InfoBaeBadRequestError
import ar.edu.unq.weather.metric.domain.exceptions.InfoBaeInternalServerError
import ar.edu.unq.weather.metric.domain.exceptions.LocalityNotFound
import io.github.resilience4j.retry.annotation.Retry
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import java.time.temporal.ChronoUnit
import kotlin.Array
import kotlin.String

@Service
class SyncHttpLoaderService: ILoaderService {
    @Autowired
    lateinit var restTemplate: RestTemplate

    private val log: Logger = LoggerFactory.getLogger(SyncHttpLoaderService::class.java)

    @Value("\${weather.loader.url}")
    private lateinit var baseURL: String

    @Retry(name = "loader-service-retry")
    override fun currentWeather(locality: Locality, unit: Unit): Weather {
        val resEntity: ResponseEntity<WeatherDTO>
            val url = "$baseURL/latest?location=${locality.toValue()}"
            this.log.info("[GET] $url")
            resEntity = restTemplate.getForEntity(url, WeatherDTO::class.java)

        return when (resEntity.statusCode) {
            HttpStatus.OK -> {
                val weather = resEntity.body!!
                log.info("[GET OK] $url")
                weather.format(unit,locality)
            }
            HttpStatus.BAD_REQUEST -> {
                log.info("[GET FAIL] BAD REQUEST: $url")
                throw InfoBaeBadRequestError()
            }
            HttpStatus.NOT_FOUND -> {
                log.info("[GET FAIL] NOT FOUND: $url")
                throw LocalityNotFound(locality.toValue())
            }
            else -> {
                log.warn("[GET FAIL] status code ${resEntity.statusCode}: $url")
                throw InfoBaeInternalServerError()
            }
        }
    }

    @Retry(name = "loader-service")
    override fun weathersBetween(locality: Locality, unit: Unit, period: Period): List<Weather> {
        val resEntity: ResponseEntity<Array<WeatherDTO>>
        val url = "$baseURL/by_period"
        try {
            this.log.info("[POST] $url. locality {}, start {}, end {}",period.location, period.startDate, period.endDate )
            resEntity = restTemplate.postForEntity(url, period, Array<WeatherDTO>::class.java)
        } catch (err: RestClientException) {
            this.log.error("[POST ERR] Loader-Service connref")
            throw ConnRefException("Loader-Service")
        }

        return when (resEntity.statusCode) {
            HttpStatus.OK -> {
                val weatherList = resEntity.body!!
                log.info("[POST OK] Weathers list size ${weatherList.size}")
                weatherList.map { Weather(
                        temperature = it.temperature,
                        unit = Unit.FAHRENHEIT,
                        sensation = it.sensation,
                        humidity = it.humidity,
                        locality = locality,
                        date = it.date.minus(3, ChronoUnit.HOURS)
                ) }
            }
            HttpStatus.BAD_REQUEST -> {
                log.info("[GET FAIL] BAD REQUEST: $url")
                throw InfoBaeBadRequestError()
            }
            HttpStatus.NOT_FOUND -> {
                log.info("[GET FAIL] NOT FOUND: $url")
                throw LocalityNotFound(locality.toValue())
            }
            else -> {
                log.warn("[GET FAIL] status code ${resEntity.statusCode}: $url")
                throw InfoBaeInternalServerError()
            }
        }
    }
}
