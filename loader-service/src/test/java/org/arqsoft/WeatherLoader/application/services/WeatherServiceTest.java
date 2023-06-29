package org.arqsoft.WeatherLoader.application.services;

import org.arqsoft.WeatherLoader.domain.exceptions.NoDataFoundException;
import org.arqsoft.WeatherLoader.domain.model.Weather;
import org.arqsoft.WeatherLoader.domain.ports.WeatherRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
public class WeatherServiceTest {

    @Mock
    private WeatherRepository weatherRepository;

    private WeatherService weatherService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        weatherService = new WeatherService();
        weatherService.weatherRepository = weatherRepository;
    }


    @Test
    public void testGetLatest() throws NoDataFoundException {
        Weather weather = new Weather();
        when(weatherRepository.getLatest("Bariloche")).thenReturn(weather);

        Weather result = weatherService.getLatest("Bariloche");

        assertEquals(weather, result);
        verify(weatherRepository, times(1)).getLatest("Bariloche");
    }

    @Test
    public void testFilterByPeriod() {
        LocalDateTime startDate = LocalDateTime.of(2023, 6, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 6, 30, 23, 59);
        List<Weather> expectedWeatherList = Arrays.asList(new Weather(), new Weather());

        when(weatherRepository.filterBetweenDates("Bariloche", startDate, endDate))
                .thenReturn(expectedWeatherList);

        List<Weather> result = weatherService.filter_by_period("Bariloche", startDate, endDate);

        assertEquals(expectedWeatherList, result);
        verify(weatherRepository, times(1))
                .filterBetweenDates("Bariloche", startDate, endDate);
    }
}