package de.koandesign.gibsgif.search.repo.converter

import de.koandesign.gibsgif.api.gfycat.entity.GfycatResponse
import de.koandesign.gibsgif.search.repo.entity.Media

class Gfycat1MbResponseConverter : Converter<List<GfycatResponse>, List<Media>> {
    override fun invoke(input: List<GfycatResponse>): List<Media> = input.mapNotNull {
        it.contentUrls?.max1mbGif?.run {
            if (url != null && width != null && height != null) {
                Media(
                    width = width.toInt(),
                    height = height.toInt(),
                    url = url
                )
            } else {
                null
            }
        }
    }
}
