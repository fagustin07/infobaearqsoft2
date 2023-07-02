package ar.edu.unq.weather.metric.domain.exceptions


open class NotFoundException(message: String): InfoBaeException(message)

class LocalityNotFound(string: String): NotFoundException("Not found $string in system")

class WeatherNotFoundException(string: String): NotFoundException(string)