package ar.edu.unq.weather.metric.infra.adapters

import ar.edu.unq.weather.metric.domain.ILoaderService
import ar.edu.unq.weather.metric.domain.Locality
import ar.edu.unq.weather.metric.domain.Unit
import ar.edu.unq.weather.metric.domain.Weather
import ar.edu.unq.weather.metric.domain.exceptions.ConnRefException
import ar.edu.unq.weather.metric.domain.exceptions.InfoBaeBadRequestError
import ar.edu.unq.weather.metric.domain.exceptions.InfoBaeInternalServerError
import com.fasterxml.jackson.annotation.JsonFormat
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

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

    override fun avgLastWeek(locality: Locality, unit: Unit): Weather {
        val resEntity: ResponseEntity<Array<WeatherDTO>>
        try {
            val dto = PeriodDTO(locality.toValue(), LocalDateTime.now().minusDays(7), LocalDateTime.now())
            val url = "$baseURL/by_period"
            this.log.info("[POST] $url. locality {}, start {}, end {}",dto.location, dto.startDate, dto.endDate )
            resEntity = restTemplate.postForEntity(url, dto, Array<WeatherDTO>::class.java)
        } catch (err: RestClientException) {
            this.log.error("[POST ERR] Loader-Service connref")
            throw ConnRefException("Loader-Service")
        }

        return when (resEntity.statusCode) {
            HttpStatus.OK -> {
                val weatherList = resEntity.body!!
                weatherList.sortByDescending{it.date}
                val weatherAcc = weatherList.reduce { total, curr -> WeatherDTO(curr.date,total.temperature + curr.temperature, total.sensation + curr.sensation, total.humidity + curr.humidity)}
                val size = weatherList.size
                val weatherRes = WeatherDTO(
                        weatherAcc.date,
                        weatherAcc.temperature/ size,
                        weatherAcc.sensation/ size,
                        weatherAcc.humidity/ size).format(unit,locality)
                log.info("[POST OK] Last week weather {}", weatherRes.toString())
                weatherRes
            }
            HttpStatus.BAD_REQUEST -> {
                log.info("[GET FAIL] BAD REQUEST")
                throw InfoBaeBadRequestError()
            }
            else -> {
                log.warn("[POST FAIL] With status ${resEntity.statusCode}")
                throw InfoBaeInternalServerError()
            }
        }
    }

    override fun lastWeekTemps(locality: Locality, unit: Unit): List<Weather> {
        val resEntity: ResponseEntity<Array<WeatherDTO>>
        try {
            val dto = PeriodDTO(locality.toValue(), LocalDateTime.now().minusDays(7), LocalDateTime.now())
            val url = "$baseURL/by_period"
            this.log.info("[POST] $url. locality {}, start {}, end {}",dto.location, dto.startDate, dto.endDate )
            resEntity = restTemplate.postForEntity(url, dto, Array<WeatherDTO>::class.java)
        } catch (err: RestClientException) {
            this.log.error("[POST ERR] Loader-Service connref")
            throw ConnRefException("Loader-Service")
        }

        return when (resEntity.statusCode) {
            HttpStatus.OK -> {
                val weatherList = resEntity.body!!
                log.info("[POST OK] Last week weather list size ${weatherList.size}")
                weatherList.sortByDescending{it.date}
                weatherList.map { it.format(unit, locality) }
            }
            HttpStatus.BAD_REQUEST -> {
                log.info("[GET FAIL] BAD REQUEST")
                throw InfoBaeBadRequestError()
            }
            else -> {
                log.warn("[POST FAIL] With status ${resEntity.statusCode}")
                throw InfoBaeInternalServerError()
            }
        }
    }

    override fun avgLastDay(locality: Locality, unit: Unit): Weather {
        val resEntity: ResponseEntity<Array<WeatherDTO>>
        try {
            val yesterdayStart = LocalDate.now().atStartOfDay().minusDays(1)
            val yesterdayEnd = yesterdayStart.plusDays(1).minusNanos(1)
            val dto = PeriodDTO(locality.toValue(), yesterdayStart, yesterdayEnd)
            val url = "$baseURL/by_period"
            this.log.info("[POST] $url. locality {}, start {}, end {}",dto.location, dto.startDate, dto.endDate )
            resEntity = restTemplate.postForEntity(url, dto, Array<WeatherDTO>::class.java)
        } catch (err: RestClientException) {
            this.log.error("[POST ERR] Loader-Service connref")
            throw ConnRefException("Loader-Service")
        }

        return when (resEntity.statusCode) {
            HttpStatus.OK -> {
                val weatherList = resEntity.body!!
                weatherList.sortByDescending{it.date}
                val weatherAcc = weatherList.reduce { total, curr -> WeatherDTO(curr.date,total.temperature + curr.temperature, total.sensation + curr.sensation, total.humidity + curr.humidity)}
                val size = weatherList.size
                val weatherRes = WeatherDTO(
                        weatherAcc.date,
                        weatherAcc.temperature/ size,
                        weatherAcc.sensation/ size,
                        weatherAcc.humidity/ size).format(unit,locality)
                log.info("[POST OK] Last week weather {}", weatherRes.toString())
                weatherRes
            }
            HttpStatus.BAD_REQUEST -> {
                log.info("[GET FAIL] BAD REQUEST")
                throw InfoBaeBadRequestError()
            }
            else -> {
                log.warn("[POST FAIL] With status ${resEntity.statusCode}")
                throw InfoBaeInternalServerError()
            }
        }
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
                sensation = parseTemp(this.sensation, unit),
                humidity = humidity,
                locality = locality,
                date = date.minus(3, ChronoUnit.HOURS)
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

data class PeriodDTO(
        val location: String,
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
        val startDate: LocalDateTime,
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
        val endDate: LocalDateTime
)