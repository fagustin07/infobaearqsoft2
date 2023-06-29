package org.arqsoft.WeatherLoader.application.services;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.arqsoft.WeatherLoader.domain.model.Location;
import org.arqsoft.WeatherLoader.domain.model.Weather;
import org.arqsoft.WeatherLoader.domain.ports.PublicDataRepository;
import org.arqsoft.WeatherLoader.domain.ports.WeatherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PublicDataService {

    @Autowired
    PublicDataRepository publicDataRepository;
    @Autowired
    WeatherRepository weatherRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("#{'${open.weather.locations}'.split(',')}")
    List<String> locations;
    @Value("#{'${open.weather.latitudes}'.split(',')}")
    List<Float> latitudes;
    @Value("#{'${open.weather.longitudes}'.split(',')}")
    List<Float> longitudes;

    @Scheduled(fixedRate = 300000)
    public void fetchWeather() throws JsonProcessingException {
        for(int i=0; i < locations.size(); i++) {
            Weather weather = publicDataRepository.fetchWeather(latitudes.get(i), longitudes.get(i), locations.get(i));
            if(weather != null) { weatherRepository.insert(weather); }
        }

    }

}
