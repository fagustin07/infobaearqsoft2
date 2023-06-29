package org.arqsoft.WeatherLoader.domain.ports;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.arqsoft.WeatherLoader.domain.model.Weather;

public interface PublicDataRepository {
    Weather fetchWeather(Float latitude, Float longitude, String name) throws JsonProcessingException;
}
