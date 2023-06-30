package ar.edu.unq.weather.metric.infra

import ar.edu.unq.weather.metric.domain.ILoaderService
import ar.edu.unq.weather.metric.infra.adapters.SyncHttpLoaderService
import ar.edu.unq.weather.metric.infra.handlers.CurrentWeatherSpringbootHandler
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.core.registry.EntryAddedEvent
import io.github.resilience4j.core.registry.EntryRemovedEvent
import io.github.resilience4j.core.registry.EntryReplacedEvent
import io.github.resilience4j.core.registry.RegistryEventConsumer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.actuate.autoconfigure.observation.ObservationAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.tracing.BraveAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.tracing.MicrometerTracingAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.web.client.RestTemplate
import java.time.Duration


@SpringBootApplication(scanBasePackages = ["ar.edu.unq.weather.metric"])
class Application

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
