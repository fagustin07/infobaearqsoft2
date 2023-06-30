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

class InfoBaeInternalServerError() : InfoBaeException("Internal server error")

class InfoBaeBadRequestError: InfoBaeException("Bad request")

class InfoBaeTooManyRequest : InfoBaeException("TOO_MANY_REQUESTS")
