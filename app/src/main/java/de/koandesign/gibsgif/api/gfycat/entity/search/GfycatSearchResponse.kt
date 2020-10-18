package de.koandesign.gibsgif.api.gfycat.entity.search

import androidx.annotation.Keep
import de.koandesign.gibsgif.api.gfycat.entity.GfycatResponse
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class GfycatSearchResponse(
    val cursor: String,
    val found: Long? = null,
    val gfycats: List<GfycatResponse>,
    val related: List<String>? = null
)
