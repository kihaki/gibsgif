package de.koandesign.gibsgif.api.gfycat.entity.trending


import androidx.annotation.Keep
import de.koandesign.gibsgif.api.gfycat.entity.GfycatResponse
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class GfycatTrendingResponse(
    val cursor: String,
    val digest: String? = null,
    val gfycats: List<GfycatResponse>,
    val tag: String? = null,
    val tagText: String? = null
)
