package ar.edu.unq.weather.metric.infra.controller

import ar.edu.unq.weather.metric.application.CurrentWeather
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

    @RequestMapping(value = ["/current"], method = [RequestMethod.GET])
    fun execute(): ResponseEntity<*>{
        return ResponseEntity(
                this.currentWeatherService.execute(),
                HttpStatus.OK
        )
    }
}