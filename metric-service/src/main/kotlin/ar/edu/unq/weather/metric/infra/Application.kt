package ar.edu.unq.weather.metric.infra

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication(scanBasePackages = ["ar.edu.unq.weather.metric"])
class Application

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
