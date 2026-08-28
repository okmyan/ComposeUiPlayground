package com.okmyan.composeuiplayground.features.instagram.di

import com.okmyan.composeuiplayground.features.instagram.data.StoriesRepository
import com.okmyan.composeuiplayground.features.instagram.data.UsersRepository
import com.okmyan.composeuiplayground.features.instagram.domain.usecases.GetUsersWithStoriesUseCase
import com.okmyan.composeuiplayground.features.instagram.domain.usecases.LikeStoriesUseCase
import com.okmyan.composeuiplayground.features.instagram.domain.usecases.SeeStoriesUseCase
import com.okmyan.composeuiplayground.features.instagram.screens.home.InstagramHomeViewModel
import com.okmyan.composeuiplayground.features.instagram.screens.story.InstagramStoryViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val instagramModule = module {
    singleOf(::UsersRepository)
    singleOf(::StoriesRepository)

    factoryOf(::GetUsersWithStoriesUseCase)
    factoryOf(::SeeStoriesUseCase)
    factoryOf(::LikeStoriesUseCase)

    viewModelOf(::InstagramHomeViewModel)
    viewModel { params ->
        InstagramStoryViewModel(
            userWithStories = params.get(),
            seeStoriesUseCase = get(),
            likeStoriesUseCase = get(),
        )
    }
}
