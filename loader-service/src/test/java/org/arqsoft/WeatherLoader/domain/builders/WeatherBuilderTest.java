package org.arqsoft.WeatherLoader.domain.builders;
import org.arqsoft.WeatherLoader.domain.model.Weather;
import org.junit.Test;
import java.time.LocalDateTime;
import static org.junit.Assert.assertEquals;

public class WeatherBuilderTest {

    @Test
    public void testWithDate() {
        LocalDateTime date = LocalDateTime.now();

        Weather weather = new WeatherBuilder()
                .withDate(date)
                .build();

        assertEquals(date, weather.getDate());
    }

    @Test
    public void testWithTemperature() {
        float temperature = 25.5f;

        Weather weather = new WeatherBuilder()
                .withTemperature(temperature)
                .build();

        assertEquals(temperature, weather.getTemperature(), 0.001f);
    }

    @Test
    public void testWithSensation() {
        float sensation = 26.2f;

        Weather weather = new WeatherBuilder()
                .withSensation(sensation)
                .build();

        assertEquals(sensation, weather.getSensation(), 0.001f);
    }

    @Test
    public void testWithHumidity() {
        float humidity = 80.0f;

        Weather weather = new WeatherBuilder()
                .withHumidity(humidity)
                .build();

        assertEquals(humidity, weather.getHumidity(), 0.001f);
    }

    @Test
    public void testWithLocation() {
        float longitude = -71.30821990966797f;
        float latitude = -41.14556884765625f;
        String locationName = "Bariloche";

        Weather weather = new WeatherBuilder()
                .withLocation(longitude, latitude, locationName)
                .build();

        assertEquals(longitude, weather.getLocation().getLongitude(), 0.001f);
        assertEquals(latitude, weather.getLocation().getLatitude(), 0.001f);
        assertEquals(locationName, weather.getLocation().getName());
    }
}