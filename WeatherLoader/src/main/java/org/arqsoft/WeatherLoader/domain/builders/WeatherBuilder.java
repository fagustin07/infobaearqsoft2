package org.arqsoft.WeatherLoader.domain.builders;

import org.arqsoft.WeatherLoader.domain.model.Location;
import org.arqsoft.WeatherLoader.domain.model.Weather;

import java.time.LocalDateTime;

public class WeatherBuilder {

    private final Weather weather;
    public WeatherBuilder() {
        this.weather = new Weather();
    }

    public WeatherBuilder withDate(LocalDateTime date) {
        this.weather.setDate(date);
        return this;
    }

    public WeatherBuilder withTemperature(Float temperature) {
        this.weather.setTemperature(temperature);
        return this;
    }

    public WeatherBuilder withSensation(Float sensation) {
        this.weather.setSensation(sensation);
        return this;
    }

    public WeatherBuilder withHumidity(Float humidity) {
        this.weather.setHumidity(humidity);
        return this;
    }

    public WeatherBuilder withLocation(Float longitude, Float latitude, String name) {
        Location location = new Location();
        location.setLongitude(longitude);
        location.setLatitude(latitude);
        location.setName(name);
        this.weather.setLocation(location);
        return this;
    }

    public Weather build() {
        return this.weather;
    }

}
