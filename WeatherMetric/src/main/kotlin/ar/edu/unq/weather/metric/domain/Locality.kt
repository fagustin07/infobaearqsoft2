package ar.edu.unq.weather.metric.domain

import ar.edu.unq.weather.metric.domain.exceptions.LocalityNotFound

enum class Locality {
    QUILMES {
        override fun toValue() = "Quilmes"
    },
    BARILOCHE {
        override fun toValue() = "Bariloche"
    },
    SAN_SALVADOR_DE_JUJUY {
        override fun toValue() = "San Salvador de Jujuy"
    };

    companion object {
        fun fromString(localityString: String): Locality {
            val locality = localityString.uppercase()
            return when (localityString.uppercase()) {
                QUILMES.toString()-> QUILMES
                BARILOCHE.toString()-> BARILOCHE
                SAN_SALVADOR_DE_JUJUY.toString()-> SAN_SALVADOR_DE_JUJUY
                else -> throw LocalityNotFound(locality)
            }
        }
    }

    abstract fun toValue(): String
}
