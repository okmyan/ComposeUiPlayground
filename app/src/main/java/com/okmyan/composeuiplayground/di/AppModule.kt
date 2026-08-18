package com.okmyan.composeuiplayground.di

import com.okmyan.composeuiplayground.features.instagram.InstagramRepository
import com.okmyan.composeuiplayground.features.instagram.InstagramStoryViewModel
import com.okmyan.composeuiplayground.features.instagram.InstagramViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::InstagramRepository)
    viewModelOf(::InstagramViewModel)
    viewModel { params -> InstagramStoryViewModel(id = params.get()) }
}
