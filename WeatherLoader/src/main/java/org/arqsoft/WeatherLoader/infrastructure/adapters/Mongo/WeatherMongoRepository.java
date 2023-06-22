package org.arqsoft.WeatherLoader.infrastructure.adapters.Mongo;

import org.arqsoft.WeatherLoader.domain.exceptions.NoDataFoundException;
import org.arqsoft.WeatherLoader.domain.model.Weather;
import org.arqsoft.WeatherLoader.domain.ports.WeatherRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface WeatherMongoRepository extends MongoRepository<Weather, String>, WeatherRepository {

    @Query(value = "{ 'location.name' : { $regex: ?0, $options: 'i' } }", sort = "{ 'date' : -1 }")
    default Weather getLatest(String location) throws NoDataFoundException {
        Pageable pageable = PageRequest.of(0, 1);
        List<Weather> results = findByLocationNameLikeOrderByDateDesc(location, pageable);
        if (results.isEmpty()) {
            throw new NoDataFoundException(location);
        }
        return results.get(0);
    }

    List<Weather> findByLocationNameLikeOrderByDateDesc(String location, Pageable pageable);

    @Query(value = "{ 'location.name' : { $regex: ?0, $options: 'i' }, 'date' : { $gte: ?1, $lte: ?2 } }")
    public List<Weather> filterBetweenDates(String location, LocalDateTime dateStart, LocalDateTime dateEnd);
}
