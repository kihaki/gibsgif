package de.koandesign.gibsgif.api.gfycat.entity

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class GfycatMediaResponse(
    val height: Long? = null,
    val size: Long? = null,
    val url: String? = null,
    val width: Long? = null
)
