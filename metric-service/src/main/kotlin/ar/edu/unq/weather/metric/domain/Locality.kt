package ar.edu.unq.weather.metric.domain

import ar.edu.unq.weather.metric.domain.exceptions.LocalityNotFound

enum class Locality {
    QUILMES {
        override fun toValue() = "Quilmes"
    },
    BARILOCHE {
        override fun toValue() = "Bariloche"
    },
    LA_PAMPA {
        override fun toValue() = "La Pampa"
    },
    SAN_SALVADOR_DE_JUJUY {
        override fun toValue() = "San Salvador de Jujuy"
    };

    abstract fun toValue(): String
}
