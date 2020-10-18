package de.koandesign.gibsgif.api.gfycat.entity


import androidx.annotation.Keep
import de.koandesign.gibsgif.api.gfycat.entity.GfycatMediaResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class GfycatContentUrlsResponse(
    val largeGif: GfycatMediaResponse? = null,
    val max1mbGif: GfycatMediaResponse? = null,
    val max2mbGif: GfycatMediaResponse? = null,
    val max5mbGif: GfycatMediaResponse? = null,
    val mobile: GfycatMediaResponse? = null,
    val mobilePoster: GfycatMediaResponse? = null,
    val mp4: GfycatMediaResponse? = null,
    @SerialName("100pxGif")
    val hundredPxGif: GfycatMediaResponse? = null,
    val webm: GfycatMediaResponse? = null,
    val webp: GfycatMediaResponse? = null
)
