package ar.edu.unq.weather.metric.domain

data class Weather(
        val degree: Int,
        val unit: Unit,
)


enum class Unit {
    FARENHEIT, CELSIUS;
}