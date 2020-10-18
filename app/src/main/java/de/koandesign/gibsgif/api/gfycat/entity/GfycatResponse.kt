package de.koandesign.gibsgif.api.gfycat.entity


import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class GfycatResponse(
    val avgColor: String? = null,
    @SerialName("content_urls")
    val contentUrls: GfycatContentUrlsResponse? = null,
    val createDate: Long? = null,
    val description: String? = null,
    val dislikes: Long? = null,
    val extraLemmas: String? = null,
    val frameRate: Double? = null,
    val gatekeeper: Long? = null,
    val gfyId: String? = null,
    val gfyName: String? = null,
    val gfyNumber: String? = null,
    val gfySlug: String? = null,
    val gif100px: String? = null,
    val gifUrl: String? = null,
    val hasAudio: Boolean? = null,
    val hasTransparency: Boolean? = null,
    val height: Long? = null,
    val languageText: String? = null,
    val likes: Long? = null,
    val max1mbGif: String? = null,
    val max2mbGif: String? = null,
    val max5mbGif: String? = null,
    val miniPosterUrl: String? = null,
    val miniUrl: String? = null,
    val mobilePosterUrl: String? = null,
    val mobileUrl: String? = null,
    val mp4Size: Long? = null,
    val mp4Url: String? = null,
    val nsfw: Long? = null,
    val numFrames: Double? = null,
    val posterUrl: String? = null,
    val published: Long? = null,
    val sitename: String? = null,
    val source: Long? = null,
    val tags: List<String>? = null,
    val thumb100PosterUrl: String? = null,
    val title: String? = null,
    val userData: GfycatUserDataResponse? = null,
    val userDisplayName: String? = null,
    val userProfileImageUrl: String? = null,
    val username: String? = null,
    val views: Long? = null,
    val webmSize: Long? = null,
    val webmUrl: String? = null,
    val webpUrl: String? = null,
    val width: Long? = null
)
