package com.okmyan.composeuiplayground.features.instagram.di

import com.okmyan.composeuiplayground.features.instagram.data.StoriesRepository
import com.okmyan.composeuiplayground.features.instagram.data.UsersRepository
import com.okmyan.composeuiplayground.features.instagram.domain.usecases.GetFeedStoriesUseCase
import com.okmyan.composeuiplayground.features.instagram.domain.usecases.GetSelfStoriesUseCase
import com.okmyan.composeuiplayground.features.instagram.domain.usecases.GetStoriesByOwnerUseCase
import com.okmyan.composeuiplayground.features.instagram.domain.usecases.LikeStoriesUseCase
import com.okmyan.composeuiplayground.features.instagram.domain.usecases.MuteUserUseCase
import com.okmyan.composeuiplayground.features.instagram.domain.usecases.SeeStoriesUseCase
import com.okmyan.composeuiplayground.features.instagram.screens.accountownerstory.AccountOwnerStoryViewModel
import com.okmyan.composeuiplayground.features.instagram.screens.feed.FeedViewModel
import com.okmyan.composeuiplayground.features.instagram.screens.story.StoryViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val instagramModule = module {
    singleOf(::UsersRepository)
    singleOf(::StoriesRepository)

    factoryOf(::GetFeedStoriesUseCase)
    factoryOf(::GetSelfStoriesUseCase)
    factoryOf(::GetStoriesByOwnerUseCase)
    factoryOf(::SeeStoriesUseCase)
    factoryOf(::LikeStoriesUseCase)
    factoryOf(::MuteUserUseCase)

    viewModelOf(::FeedViewModel)
    viewModelOf(::AccountOwnerStoryViewModel)
    viewModel { params ->
        StoryViewModel(
            selectedStoryOwnerId = params.get(),
            getStoriesByOwnerUseCase = get(),
            seeStoriesUseCase = get(),
            likeStoriesUseCase = get(),
            muteUserUseCase = get(),
        )
    }
}
