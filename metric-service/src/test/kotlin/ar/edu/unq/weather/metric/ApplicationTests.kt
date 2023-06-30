package ar.edu.unq.weather.metric

import ar.edu.unq.weather.metric.infra.Application
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [Application::class])
class ApplicationTests {

	@Test
	fun contextLoads() {
	}

}
