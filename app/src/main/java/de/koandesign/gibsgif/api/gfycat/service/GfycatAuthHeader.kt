package de.koandesign.gibsgif.api.gfycat.service

import okhttp3.Interceptor
import okhttp3.Response
import kotlin.properties.ReadWriteProperty

class GfycatAuthHeader(tokenStore: ReadWriteProperty<Any?, String>) : Interceptor {
    private val authToken: String by tokenStore

    override fun intercept(chain: Interceptor.Chain): Response {
        with(chain.request().newBuilder()) {
            if (authToken.isNotEmpty()) {
                addAuthToken(authToken)
            }
            return chain.proceed(build())
        }
    }
}

