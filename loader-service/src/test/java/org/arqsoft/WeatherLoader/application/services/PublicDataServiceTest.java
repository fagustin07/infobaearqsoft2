package org.arqsoft.WeatherLoader.application.services;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.arqsoft.WeatherLoader.domain.model.Weather;
import org.arqsoft.WeatherLoader.domain.ports.PublicDataRepository;
import org.arqsoft.WeatherLoader.domain.ports.WeatherRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.mockito.Mockito.*;

public class PublicDataServiceTest {

    @Mock
    private PublicDataRepository publicDataRepository;
    @Mock
    private WeatherRepository weatherRepository;

    private PublicDataService publicDataService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        publicDataService = new PublicDataService();
        publicDataService.publicDataRepository = publicDataRepository;
        publicDataService.weatherRepository = weatherRepository;
        publicDataService.locations = Arrays.asList("Bariloche", "Buenos Aires");
        publicDataService.latitudes = Arrays.asList(-41.14556884765625f, -34.603722f);
        publicDataService.longitudes = Arrays.asList(-71.30821990966797f, -58.381592f);
    }

    @After
    public void tearDown() {
        reset(weatherRepository);
    }

    @Test
    public void testFetchWeather() throws JsonProcessingException {
        Weather weather1 = new Weather();
        Weather weather2 = new Weather();
        when(publicDataRepository.fetchWeather(-41.14556884765625f, -71.30821990966797f, "Bariloche"))
                .thenReturn(weather1);
        when(publicDataRepository.fetchWeather(-34.603722f, -58.381592f, "Buenos Aires"))
                .thenReturn(weather2);

        publicDataService.fetchWeather();

        verify(publicDataRepository, times(1))
                .fetchWeather(-41.14556884765625f, -71.30821990966797f, "Bariloche");
        verify(publicDataRepository, times(1))
                .fetchWeather(-34.603722f, -58.381592f, "Buenos Aires");
        verify(weatherRepository, times(2)).insert(weather1);
        verify(weatherRepository, times(2)).insert(weather2);
    }
}