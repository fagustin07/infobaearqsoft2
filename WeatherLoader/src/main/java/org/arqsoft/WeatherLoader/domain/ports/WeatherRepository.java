package org.arqsoft.WeatherLoader.domain.ports;
import org.arqsoft.WeatherLoader.domain.exceptions.NoDataFoundException;
import org.arqsoft.WeatherLoader.domain.model.Weather;
import java.time.LocalDateTime;
import java.util.List;

public interface WeatherRepository {

    Weather getLatest(String location) throws NoDataFoundException;

    List<Weather> filterBetweenDates(String location, LocalDateTime dateStart, LocalDateTime dateEnd);

    Weather insert(Weather weather);

}
