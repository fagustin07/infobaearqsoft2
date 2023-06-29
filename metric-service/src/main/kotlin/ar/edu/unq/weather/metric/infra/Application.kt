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

@Configuration
class BaseConfig {
	@Bean
	fun restTemplate(builder: RestTemplateBuilder): RestTemplate {
		return builder
				.setConnectTimeout(Duration.ofSeconds(1))
				.setReadTimeout(Duration.ofSeconds(2))
				.build()
	}


	@Bean
	fun loaderService(): ILoaderService {
		return SyncHttpLoaderService()
	}

}


@Configuration
@Import(value = [
	BraveAutoConfiguration::class,
	MicrometerTracingAutoConfiguration::class,
	ObservationAutoConfiguration::class
])
class TracingConfig


@Configuration
class CircuitBreakerConfig {

	private val log: Logger = LoggerFactory.getLogger(CircuitBreakerConfig::class.java)
	@Bean
	fun circuitBreakerEventConsumer(): RegistryEventConsumer<CircuitBreaker> {
		return object : RegistryEventConsumer<CircuitBreaker> {
			override fun onEntryAddedEvent(entryAddedEvent: EntryAddedEvent<CircuitBreaker>) {
				entryAddedEvent.addedEntry.eventPublisher
						.onFailureRateExceeded { event ->
							log.error("circuit breaker {} failure rate {} on {}",
									event.circuitBreakerName, event.failureRate, event.creationTime)
						}
						.onSlowCallRateExceeded { event ->
							log.error("circuit breaker {} slow call rate {} on {}",
									event.circuitBreakerName, event.slowCallRate, event.creationTime)
						}
						.onCallNotPermitted { event ->
							log.error("circuit breaker {} call not permitted {}",
									event.circuitBreakerName, event.creationTime)
						}
						.onError { event ->
							log.error("circuit breaker {} error with duration {}s",
									event.circuitBreakerName, event.elapsedDuration.seconds)
						}
						.onStateTransition { event ->
							log.warn("circuit breaker {} state transition from {} to {} on {}",
									event.circuitBreakerName, event.stateTransition.fromState,
									event.stateTransition.toState, event.creationTime)
						}
			}

			override fun onEntryRemovedEvent(entryRemoveEvent: EntryRemovedEvent<CircuitBreaker>) {
				entryRemoveEvent.removedEntry.eventPublisher
						.onFailureRateExceeded { event ->
							log.debug("Circuit breaker event removed {}",
									event.circuitBreakerName)
						}
			}

			override fun onEntryReplacedEvent(entryReplacedEvent: EntryReplacedEvent<CircuitBreaker>) {
				entryReplacedEvent.newEntry.eventPublisher
						.onFailureRateExceeded { event ->
							log.debug("Circuit breaker event replaced {}",
									event.circuitBreakerName)
						}
			}
		}
	}
}
