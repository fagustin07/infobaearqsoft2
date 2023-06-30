package ar.edu.unq.weather.metric.infra.config

import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.core.registry.EntryAddedEvent
import io.github.resilience4j.core.registry.EntryRemovedEvent
import io.github.resilience4j.core.registry.EntryReplacedEvent
import io.github.resilience4j.core.registry.RegistryEventConsumer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CircuitBreakerLogger {

    private val log: Logger = LoggerFactory.getLogger(CircuitBreakerLogger::class.java)

    @Bean
    fun circuitBreakerEventConsumer(): RegistryEventConsumer<CircuitBreaker> {
        return object : RegistryEventConsumer<CircuitBreaker> {
            override fun onEntryAddedEvent(entryAddedEvent: EntryAddedEvent<CircuitBreaker>) {
                entryAddedEvent.addedEntry.eventPublisher
                        .onFailureRateExceeded { event ->
                            log.error("[CIRCUIT BREAKER] {} failure rate {} on {}",
                                    event.circuitBreakerName, event.failureRate, event.creationTime)
                        }
                        .onSlowCallRateExceeded { event ->
                            log.error("[CIRCUIT BREAKER] {} slow call rate {} on {}",
                                    event.circuitBreakerName, event.slowCallRate, event.creationTime)
                        }
                        .onCallNotPermitted { event ->
                            log.error("[CIRCUIT BREAKER] {} call not permitted {}",
                                    event.circuitBreakerName, event.creationTime)
                        }
                        .onError { event ->
                            log.warn("[CIRCUIT BREAKER] {} error with duration {}ms",
                                    event.circuitBreakerName, (event.elapsedDuration.nano/1000000).toInt())
                        }
                        .onStateTransition { event ->
                            log.warn("[CIRCUIT BREAKER] {} state transition from {} to {} on {}",
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