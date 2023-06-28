package ar.edu.unq.weather.metric.infra

import ar.edu.unq.weather.metric.domain.ILoaderService
import ar.edu.unq.weather.metric.infra.adapters.SyncHttpLoaderService
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

