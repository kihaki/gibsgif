package de.koandesign.gibsgif.search.di

import de.koandesign.gibsgif.api.gfycat.entity.GfycatResponse
import de.koandesign.gibsgif.search.di.Names.MOBILE_POSTER_CONVERTER
import de.koandesign.gibsgif.search.di.Names.TRENDING_1MB_CONVERTER
import de.koandesign.gibsgif.search.repo.converter.Converter
import de.koandesign.gibsgif.search.repo.FeedRepository
import de.koandesign.gibsgif.search.repo.converter.Gfycat1MbResponseConverter
import de.koandesign.gibsgif.search.repo.converter.MobilePosterUrlConverter
import de.koandesign.gibsgif.search.repo.entity.Media
import de.koandesign.gibsgif.search.ui.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

private object Names {
    const val TRENDING_1MB_CONVERTER = "TRENDING_1MB_CONVERTER"
    const val MOBILE_POSTER_CONVERTER = "MOBILE_POSTER_CONVERTER"
}

val searchModule = module {
    single<Converter<List<GfycatResponse>, List<Media>>>(named(TRENDING_1MB_CONVERTER)) { Gfycat1MbResponseConverter() }
    single<Converter<List<GfycatResponse>, List<Media>>>(named(MOBILE_POSTER_CONVERTER)) { MobilePosterUrlConverter() }
    single { FeedRepository(get(), get(named(TRENDING_1MB_CONVERTER))) }
    viewModel { SearchViewModel(get()) }
}
