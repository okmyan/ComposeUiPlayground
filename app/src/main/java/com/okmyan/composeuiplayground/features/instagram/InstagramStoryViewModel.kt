package com.okmyan.composeuiplayground.features.instagram

import androidx.lifecycle.ViewModel
import timber.log.Timber

class InstagramStoryViewModel(
    val id: Long,
) : ViewModel() {

    init {
        Timber.d("Init block $id")
    }

    override fun onCleared() {
        Timber.d("clear $id")
    }
}
