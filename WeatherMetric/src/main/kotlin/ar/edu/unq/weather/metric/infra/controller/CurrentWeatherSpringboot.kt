package ar.edu.unq.weather.metric.infra.controller

import ar.edu.unq.weather.metric.application.CurrentWeather
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class CurrentWeatherSpringboot {
    @Autowired
    private lateinit var currentWeatherService: CurrentWeather

    private val log: Logger = LoggerFactory.getLogger(CurrentWeatherSpringboot::class.java)
    @RequestMapping(value = ["/current"], method = [RequestMethod.GET])
    fun execute(): ResponseEntity<*>{
        val res = this.currentWeatherService.execute()
        this.log.info("attaching new weather {}", res)
        return ResponseEntity(
                res,
                HttpStatus.OK
        )
    }
}