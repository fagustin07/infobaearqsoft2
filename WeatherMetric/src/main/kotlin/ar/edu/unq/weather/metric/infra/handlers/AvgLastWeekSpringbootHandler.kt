package ar.edu.unq.weather.metric.infra.handlers

import ar.edu.unq.weather.metric.application.AvgLastWeekWeatherService
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
class AvgLastWeekSpringbootHandler {

    private val log: Logger = LoggerFactory.getLogger(CurrentWeatherSpringbootHandler::class.java)

    @Autowired
    private lateinit var avgLastWeakWeatherService : AvgLastWeekWeatherService
    @RequestMapping(value = ["/weather/lastweek/avg"], method = [RequestMethod.POST])
    fun execute(
            @RequestParam("locality") locality : Locality,
            @RequestParam("unit") unit : Unit
    ): ResponseEntity<*> {
        this.log.info("[REQUEST] /api/v1/weather/lastweek/avg?locality=${locality}&unit=$unit")

        return try {
            val res = this.avgLastWeakWeatherService.execute(locality, unit)
            this.log.info("[RESPONSE-OK] /api/v1/weather/lastweek/avg?locality=${locality}&unit=$unit")
            ResponseEntity(
                    res,
                    HttpStatus.OK
            )
        } catch (e: Exception) {
            this.log.error("[RESPONSE-FAILED] ${e.message} /api/v1//weather/lastweek/avg?locality=${locality}&unit=$unit")
            ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
        }
    }
}
