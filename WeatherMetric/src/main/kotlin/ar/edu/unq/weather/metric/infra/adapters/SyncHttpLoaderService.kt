package ar.edu.unq.weather.metric.infra.adapters

import ar.edu.unq.weather.metric.domain.*
import ar.edu.unq.weather.metric.domain.Unit
import ar.edu.unq.weather.metric.domain.exceptions.ConnRefException
import ar.edu.unq.weather.metric.domain.exceptions.InfoBaeBadRequestError
import ar.edu.unq.weather.metric.domain.exceptions.InfoBaeInternalServerError
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate

@Service
class SyncHttpLoaderService: ILoaderService {
    @Autowired
    lateinit var restTemplate: RestTemplate

    private val log: Logger = LoggerFactory.getLogger(SyncHttpLoaderService::class.java)

    @Value("\${weather.loader.url}")
    private lateinit var baseURL: String

    override fun currentWeather(locality: Locality, unit: Unit): Weather {
        val resEntity: ResponseEntity<WeatherDTO>
        try {
            val url = "$baseURL/latest?location=${locality.toValue()}"
            this.log.info("[GET] $url")
            resEntity = restTemplate.getForEntity(url, WeatherDTO::class.java)
        } catch (err: RestClientException) {
            this.log.error("[GET FAIL] Loader-Service Connection refused")
            throw ConnRefException("Loader-Service")
        }

        return when (resEntity.statusCode) {
            HttpStatus.OK -> {
                val weather = resEntity.body!!
                log.info("[GET OK] successfully")
                weather.format(unit,locality)
            }
            HttpStatus.BAD_REQUEST -> {
                log.info("[GET FAIL] BAD REQUEST")
                throw InfoBaeBadRequestError()
            }
            else -> {
                log.warn("[GET FAIL] Failed with status ${resEntity.statusCode}")
                throw InfoBaeInternalServerError()
            }
        }
    }

    override fun weathersBetween(locality: Locality, unit: Unit, period: Period): List<Weather> {
        val resEntity: ResponseEntity<Array<WeatherDTO>>
        try {
            val url = "$baseURL/by_period"
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
                weatherList.sortByDescending{it.date}
                weatherList.map { it.format(unit, locality) }
            }
            HttpStatus.BAD_REQUEST -> {
                log.info("[POST FAIL] BAD REQUEST")
                throw InfoBaeBadRequestError()
            }
            else -> {
                log.warn("[POST FAIL] With status ${resEntity.statusCode}")
                throw InfoBaeInternalServerError()
            }
        }
    }
}
