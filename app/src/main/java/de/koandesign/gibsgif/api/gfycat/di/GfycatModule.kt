package de.koandesign.gibsgif.api.gfycat.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import de.koandesign.gibsgif.BuildConfig
import de.koandesign.gibsgif.api.gfycat.service.GfycatAuthHeader
import de.koandesign.gibsgif.api.gfycat.GfycatApi
import de.koandesign.gibsgif.api.gfycat.service.GfycatAuthenticator
import de.koandesign.gibsgif.api.gfycat.service.GfycatService
import de.koandesign.gibsgif.api.gfycat.di.GfycatModuleNames.AUTH_INTERCEPTOR
import de.koandesign.gibsgif.api.gfycat.di.GfycatModuleNames.AUTH_TOKEN_STORE
import de.koandesign.gibsgif.api.gfycat.entity.auth.AuthRequest
import de.koandesign.gibsgif.storage.SharedPrefsReadWriteProperty
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import okhttp3.Authenticator
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import kotlin.properties.ReadWriteProperty

private object GfycatModuleNames {
    const val AUTH_TOKEN_STORE = "AUTH_TOKEN_STORE"
    const val AUTH_INTERCEPTOR = "AUTH_INTERCEPTOR"
}

val gfycatModule = module {
    single<ReadWriteProperty<Any?, String>>(named(AUTH_TOKEN_STORE)) {
        // TODO: Replace with encrypted storage
        SharedPrefsReadWriteProperty(
            prefs = get(),
            serializer = String.serializer(),
            valueKey = "gfycat_auth_token",
            emptyValue = ""
        )
    }
    single(named(AUTH_INTERCEPTOR)) { GfycatAuthHeader(tokenStore = get(named(AUTH_TOKEN_STORE))) }
    single {
        AuthRequest.ClientCredentials(
            clientId = BuildConfig.GFYCAT_CLIENT_ID,
            clientSecret = BuildConfig.GFYCAT_CLIENT_SECRET
        )
    }
    single<Authenticator> {
        GfycatAuthenticator(
            tokenStore = get(named(AUTH_TOKEN_STORE)),
            authRequest = get()
        )
    }
    single<GfycatService> {
        Retrofit.Builder()
            .baseUrl(GfycatService.BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .apply {
                        if (BuildConfig.DEBUG) {
                            addInterceptor(HttpLoggingInterceptor().apply { setLevel(Level.BODY) })
                        }
                    }
                    .addInterceptor(get<GfycatAuthHeader>(named(AUTH_INTERCEPTOR)))
                    .authenticator(get())
                    .build()
            )
            .addConverterFactory(
                Json {
                    ignoreUnknownKeys = true
                }.asConverterFactory("application/json".toMediaType())
            )
            .build()
            .create(GfycatService::class.java)
    }
    single { GfycatApi(get()) }
}
