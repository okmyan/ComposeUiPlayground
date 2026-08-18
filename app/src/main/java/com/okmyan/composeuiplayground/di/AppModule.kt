package com.okmyan.composeuiplayground.di

import com.okmyan.composeuiplayground.features.instagram.InstagramRepository
import com.okmyan.composeuiplayground.features.instagram.story.InstagramStoryViewModel
import com.okmyan.composeuiplayground.features.instagram.home.InstagramHomeViewModel
import com.okmyan.composeuiplayground.features.instagram.home.UserWithStories
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::InstagramRepository)
    viewModelOf(::InstagramHomeViewModel)
    viewModel { params -> InstagramStoryViewModel(userWithStories = params.get()) }
}
