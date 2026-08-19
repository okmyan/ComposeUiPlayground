package com.okmyan.composeuiplayground.utils.extensions

fun <T> Iterator<T>.findNext(predicate: (T) -> Boolean): T? {
    while (hasNext()) {
        if (predicate(next())) {
            return if (hasNext()) next() else null
        }
    }

    return null
}
