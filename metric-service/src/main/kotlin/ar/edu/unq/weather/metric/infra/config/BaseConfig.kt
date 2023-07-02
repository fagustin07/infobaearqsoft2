package ar.edu.unq.weather.metric.infra.config

import ar.edu.unq.weather.metric.domain.ILoaderService
import ar.edu.unq.weather.metric.infra.adapters.SyncHttpLoaderService
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import java.time.Duration

@Configuration
class BaseConfig {
    @Bean
    fun restTemplate(builder: RestTemplateBuilder): RestTemplate {
        return builder
                .setConnectTimeout(Duration.ofSeconds(2 * 1000))
                .setReadTimeout(Duration.ofSeconds(2 * 1000))
                .build()
    }


    @Bean
    fun loaderService(): ILoaderService {
        return SyncHttpLoaderService()
    }
}