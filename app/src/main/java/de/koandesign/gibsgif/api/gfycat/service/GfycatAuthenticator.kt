package de.koandesign.gibsgif.api.gfycat.service

import de.koandesign.gibsgif.api.gfycat.entity.auth.AuthRequest
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.koin.java.KoinJavaComponent.inject
import kotlin.properties.ReadWriteProperty

class GfycatAuthenticator(
    tokenStore: ReadWriteProperty<Any?, String>,
    private val authRequest: AuthRequest.ClientCredentials
) : Authenticator {
    private var authToken: String by tokenStore

    // This needs to be lazy to avoid circular dependencies
    private val authService: GfycatService by inject(GfycatService::class.java)

    override fun authenticate(route: Route?, response: Response): Request? {
        val previousAuthToken = authToken
        if (!response.isRequestWithAuth || previousAuthToken.isEmpty()) {
            return null
        }
        synchronized(this) {
            val currentAuthToken = authToken
            if (currentAuthToken != previousAuthToken && currentAuthToken.isNotEmpty()) {
                // Auth token was refreshed in another thread
                return response.request.updateAuthToken(currentAuthToken)
            }

            // Auth token requires refresh
            val updatedAuthToken = runBlocking {
                val authResponse = authService.getAuthToken(authRequest)
                authResponse.body()?.accessToken
            }

            return if (updatedAuthToken != null) {
                authToken = updatedAuthToken
                response.request.updateAuthToken(updatedAuthToken)
            } else {
                null
            }
        }
    }

    private fun Request.updateAuthToken(authToken: String): Request = newBuilder()
        .addAuthToken(authToken)
        .build()

    private val Response.isRequestWithAuth: Boolean
        get() = request.header("Authorization")?.startsWith("Bearer") ?: false
}
