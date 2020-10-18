package de.koandesign.gibsgif.api.gfycat.entity.auth

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed class AuthRequest {
    abstract val grantType: String

    @Keep
    @Serializable
    data class ClientCredentials(
        @SerialName("client_id")
        val clientId: String,
        @SerialName("client_secret")
        val clientSecret: String,
    ) : AuthRequest() {
        @SerialName("grant_type")
        override val grantType: String = "client_credentials"
    }
}
