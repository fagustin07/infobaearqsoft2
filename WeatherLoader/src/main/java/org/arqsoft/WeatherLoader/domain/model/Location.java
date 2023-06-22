package org.arqsoft.WeatherLoader.domain.model;

import lombok.Data;

@Data
public class Location {
    private Float longitude;
    private Float latitude;
    private String name;
}
