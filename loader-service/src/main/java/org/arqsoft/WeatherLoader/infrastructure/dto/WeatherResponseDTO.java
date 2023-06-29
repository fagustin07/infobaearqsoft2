package org.arqsoft.WeatherLoader.infrastructure.dto;
import lombok.Data;


@Data
public class WeatherResponseDTO {
    private Float temp;
    private Float feels_like;
    private Float humidity;
}
