package ar.edu.unq.weather.metric.infra.controller

import ar.edu.unq.weather.metric.application.CurrentWeather
import ar.edu.unq.weather.metric.domain.Locality
import ar.edu.unq.weather.metric.domain.Unit
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class CurrentWeatherSpringboot {
    @Autowired
    private lateinit var currentWeatherService: CurrentWeather

    private val log: Logger = LoggerFactory.getLogger(CurrentWeatherSpringboot::class.java)
    @RequestMapping(value = ["/weather/current"], method = [RequestMethod.GET])
    fun execute(
            @RequestParam("locality") locality : Locality,
            @RequestParam("unit") unit : Unit,
    ): ResponseEntity<*>{
        this.log.info("[REQUEST] /api/v1/weather/latest?locality=${locality}&unit=$unit")

        return try {
            val res = this.currentWeatherService.execute(locality, unit)
            this.log.info("[RESPONSE-OK] /api/v1/weather/latest?locality=${locality}&unit=$unit")
            ResponseEntity(
                    res,
                    HttpStatus.OK
            )
        } catch (e: Exception) {
            this.log.error("[RESPONSE-FAILED] ${e.message} /api/v1/weather/latest?locality=${locality}&unit=$unit")
            ResponseEntity("sdkajsdkjas", HttpStatus.OK)
        }
    }
}