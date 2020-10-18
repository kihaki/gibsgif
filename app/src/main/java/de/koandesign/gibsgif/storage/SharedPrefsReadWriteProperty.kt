package de.koandesign.gibsgif.storage

import android.content.SharedPreferences
import kotlinx.serialization.KSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SharedPrefsReadWriteProperty<R, T>(
    private val prefs: SharedPreferences,
    private val serializer: KSerializer<T>,
    private val valueKey: String,
    private val emptyValue: T
) : ReadWriteProperty<R, T> {
    override fun setValue(thisRef: R, property: KProperty<*>, value: T) =
        prefs.edit().putString(valueKey, Json.encodeToString(serializer)).apply()

    override fun getValue(thisRef: R, property: KProperty<*>): T =
        prefs.getString(valueKey, null)?.let { Json.decodeFromString(serializer, it) } ?: emptyValue
}
