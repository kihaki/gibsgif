package de.koandesign.gibsgif.api.gfycat.entity


import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class GfycatUserDataResponse(
    val followers: Long? = null,
    val following: Long? = null,
    val name: String? = null,
    val profileImageUrl: String? = null,
    val subscription: Long? = null,
    val username: String? = null,
    val verified: Boolean? = null,
    val views: Long? = null
)
