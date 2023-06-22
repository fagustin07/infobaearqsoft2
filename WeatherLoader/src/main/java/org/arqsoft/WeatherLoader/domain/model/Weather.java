package org.arqsoft.WeatherLoader.domain.model;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document
public class Weather {
    private LocalDateTime date;
    private Float temperature;
    private Float sensation;
    private Float humidity;
    private Location location;
}
