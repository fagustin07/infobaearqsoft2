package ar.edu.unq.weather.metric.domain.exceptions

abstract class InfoBaeException(msg: String): RuntimeException(msg) {

    fun toMap(): Map<String, String> {
        return mapOf(
                Pair("exception", this.javaClass.simpleName),
                Pair("message", this.message!!)
        )
    }
}

class ConnRefException(service: String): InfoBaeException("$service not available")

class InfoBaeInternalServerError(err: String = "Internal Server error") : InfoBaeException(err)

class InfoBaeBadRequestError(err: String = "Bad Request error"): InfoBaeException(err)

class InfoBaeTooManyRequest : InfoBaeException("TOO_MANY_REQUESTS")
