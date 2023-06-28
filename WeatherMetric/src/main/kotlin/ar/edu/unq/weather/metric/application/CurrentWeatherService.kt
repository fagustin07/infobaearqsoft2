package ar.edu.unq.weather.metric.application

import ar.edu.unq.weather.metric.domain.ILoaderService
import ar.edu.unq.weather.metric.domain.Locality
import ar.edu.unq.weather.metric.domain.Unit
import ar.edu.unq.weather.metric.domain.Weather
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

@Service
class CurrentWeatherService {

    @Autowired
    lateinit var loaderService: ILoaderService

    fun execute(locality: Locality, unit: Unit): Weather {
        return loaderService.currentWeather(locality, unit)
    }
}
