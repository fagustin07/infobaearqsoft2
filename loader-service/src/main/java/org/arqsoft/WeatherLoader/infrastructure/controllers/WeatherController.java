package org.arqsoft.WeatherLoader.infrastructure.controllers;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.arqsoft.WeatherLoader.application.services.PublicDataService;
import org.arqsoft.WeatherLoader.application.services.WeatherService;
import org.arqsoft.WeatherLoader.domain.exceptions.NoDataFoundException;
import org.arqsoft.WeatherLoader.domain.exceptions.TimeoutException;
import org.arqsoft.WeatherLoader.domain.model.Weather;
import org.arqsoft.WeatherLoader.infrastructure.dto.PeriodDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/weather")
public class WeatherController {
    @Autowired
    private WeatherService weatherService;

    @Autowired
    private PublicDataService publicDataService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @GetMapping("/latest")
    @RateLimiter(name = "latest")
    Weather getLatest(@RequestParam(name="location") String location) throws NoDataFoundException {
        this.logger.info("[REQUEST] /api/v1/weather/latest?location=" + location);
        return weatherService.getLatest(location);
    }

    @PostMapping("/by_period")
    @ResponseBody
    @RateLimiter(name = "by_period")
    List<Weather> filter_by_period(@RequestBody PeriodDTO periodDTO) {
        this.logger.info("[REQUEST] /api/v1/weather/by_period", periodDTO);
        return weatherService.filter_by_period(periodDTO.getLocation(), periodDTO.getStartDate(), periodDTO.getEndDate());
    }

}
