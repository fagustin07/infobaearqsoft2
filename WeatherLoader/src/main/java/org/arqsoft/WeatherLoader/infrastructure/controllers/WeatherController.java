package org.arqsoft.WeatherLoader.infrastructure.controllers;

import lombok.RequiredArgsConstructor;
import org.arqsoft.WeatherLoader.application.services.PublicDataService;
import org.arqsoft.WeatherLoader.application.services.WeatherService;
import org.arqsoft.WeatherLoader.domain.exceptions.NoDataFoundException;
import org.arqsoft.WeatherLoader.domain.model.Weather;
import org.arqsoft.WeatherLoader.infrastructure.dto.PeriodDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/weather")
public class WeatherController {
    @Autowired
    private WeatherService weatherService;

    @Autowired
    private PublicDataService publicDataService;

    @GetMapping("/latest")
    Weather getLatest(@RequestParam(name="location") String location) throws NoDataFoundException {
        return weatherService.getLatest(location);
    }

    @PostMapping("/by_period")
    @ResponseBody
    List<Weather> filter_by_period(@RequestBody PeriodDTO periodDTO) {
        return weatherService.filter_by_period(periodDTO.getLocation(), periodDTO.getStartDate(), periodDTO.getEndDate());
    }

}
