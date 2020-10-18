package de.koandesign.gibsgif.storage.di

import android.content.Context
import android.content.SharedPreferences
import de.koandesign.gibsgif.BuildConfig
import org.koin.dsl.module

val storageModule = module {
    single<SharedPreferences> {
        get<Context>().getSharedPreferences(
            "${BuildConfig.APPLICATION_ID}.shared_preferences",
            Context.MODE_PRIVATE
        )
    }
}
