package ar.edu.unq.weather.metric.infra.handlers

import ar.edu.unq.weather.metric.application.CurrentWeatherService
import ar.edu.unq.weather.metric.domain.Locality
import ar.edu.unq.weather.metric.domain.Unit
import ar.edu.unq.weather.metric.infra.ServiceREST
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam

@ServiceREST
@RequestMapping("/api/v1")
class CurrentWeatherSpringbootHandler {
    @Autowired
    private lateinit var currentWeatherService: CurrentWeatherService

    private val log: Logger = LoggerFactory.getLogger(CurrentWeatherSpringbootHandler::class.java)
    @RequestMapping(value = ["/weather/current"], method = [RequestMethod.GET])
    fun execute(
            @RequestParam("locality", required = false) locality : Locality? = null,
            @RequestParam("unit", required = false) unit : Unit? = null
    ): ResponseEntity<*>{
        this.log.info("[REQUEST] /api/v1/weather/latest?locality=${locality}&unit=$unit")

        return try {
            val res = this.currentWeatherService.execute(locality ?: Locality.QUILMES, unit ?: Unit.CELSIUS)
            this.log.info("[RESPONSE-OK] /api/v1/weather/latest?locality=${locality}&unit=$unit")
            ResponseEntity(
                    res,
                    HttpStatus.OK
            )
        } catch (e: Exception) {
            this.log.error("[RESPONSE-FAILED] ${e.message} /api/v1/weather/latest?locality=${locality}&unit=$unit")
            ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
        }
    }
}