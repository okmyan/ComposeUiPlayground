package com.okmyan.composeuiplayground.features.instagram.domain.usecases

import com.okmyan.composeuiplayground.features.instagram.data.UsersRepository

class MuteUserUseCase(
    private val usersRepository: UsersRepository,
) {

    suspend operator fun invoke(userId: Long) {
        usersRepository.muteUser(userId)
    }

}
