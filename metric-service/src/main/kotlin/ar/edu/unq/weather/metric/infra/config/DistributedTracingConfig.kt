package ar.edu.unq.weather.metric.infra.config

import org.springframework.boot.actuate.autoconfigure.observation.ObservationAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.tracing.BraveAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.tracing.MicrometerTracingAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(value = [
    BraveAutoConfiguration::class,
    MicrometerTracingAutoConfiguration::class,
    ObservationAutoConfiguration::class
])
class DistributedTracingConfig
