package org.arqsoft.WeatherLoader.infrastructure.controllers;

import lombok.RequiredArgsConstructor;
import org.arqsoft.WeatherLoader.domain.exceptions.NoDataFoundException;
import org.arqsoft.WeatherLoader.domain.model.Weather;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class RootController {
    @GetMapping("/")
    String getLatest() {
        return "Service Weather Loader working.";
    }
}
