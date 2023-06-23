package org.arqsoft.WeatherLoader.infrastructure.adapters.OpenWeatherAPI;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.arqsoft.WeatherLoader.domain.builders.WeatherBuilder;
import org.arqsoft.WeatherLoader.domain.model.Weather;
import org.arqsoft.WeatherLoader.domain.ports.PublicDataRepository;
import org.arqsoft.WeatherLoader.infrastructure.dto.WeatherResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import java.time.Duration;
import java.time.LocalDateTime;

@Repository
public class OpenWeatherRepository implements PublicDataRepository {

    private final RestTemplate restTemplate;
    @Value("${open.weather.uri}")
    private String api_uri;
    @Value("${open.weather.api.key}")
    private String api_key;
    private static final ObjectMapper mapper = new ObjectMapper();

    public OpenWeatherRepository() {
        restTemplate = new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofMillis(3000))
                .setReadTimeout(Duration.ofMillis(3000))
                .build();
    }

    @Override
    public Weather fetchWeather(Float latitude, Float longitude, String name) throws JsonProcessingException {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        WeatherResponseDTO weatherResponse;

        String response = this.restTemplate.getForEntity(api_uri + "?lat=" + latitude + "&lon=" + longitude + "&appid=" + api_key, String.class).getBody();

        JsonNode jsonResponse = mapper.readTree(response);
        JsonNode jsonNested = jsonResponse.get("main");
        weatherResponse = mapper.treeToValue(jsonNested, WeatherResponseDTO.class);

        return new WeatherBuilder()
                .withSensation(weatherResponse.getFeels_like())
                .withTemperature(weatherResponse.getTemp())
                .withHumidity(weatherResponse.getHumidity())
                .withLocation(longitude, latitude, name)
                .withDate(LocalDateTime.now())
                .build();
    }
}
