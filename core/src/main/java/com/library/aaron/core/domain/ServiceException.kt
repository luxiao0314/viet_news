package com.library.aaron.core.domain

class ServiceException(message: String) : Exception(message) {
    val error: ErrorEnvelope

    init {

        error = ErrorEnvelope(message)
    }
}
