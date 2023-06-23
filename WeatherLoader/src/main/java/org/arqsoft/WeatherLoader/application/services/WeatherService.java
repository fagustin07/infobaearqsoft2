package org.arqsoft.WeatherLoader.application.services;
import org.arqsoft.WeatherLoader.domain.exceptions.NoDataFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.arqsoft.WeatherLoader.domain.model.Weather;
import org.arqsoft.WeatherLoader.domain.ports.WeatherRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class WeatherService {

    @Autowired
    WeatherRepository weatherRepository;

    @Value("${open.weather.latitude}")
    private String latitude;
    @Value("${open.weather.longitude}")
    private String longitude;

    public Weather getLatest(String location) throws NoDataFoundException {
        return weatherRepository.getLatest(location);
    }

    public List<Weather> filter_by_period(String location, LocalDateTime startDate, LocalDateTime endDate) {
        return weatherRepository.filterBetweenDates(location, startDate, endDate);
    }

}
