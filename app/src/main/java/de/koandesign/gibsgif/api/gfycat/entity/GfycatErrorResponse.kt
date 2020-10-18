package de.koandesign.gibsgif.api.gfycat.entity

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class GfycatErrorResponse(
    val errorMessage: ErrorMessage
) {
    @Keep
    @Serializable
    data class ErrorMessage(
        val code: String,
        val description: String
    )
}
