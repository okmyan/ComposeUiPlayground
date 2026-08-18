package com.okmyan.composeuiplayground.utils.extensions

fun <T> Iterator<T>.findNext(element: T): T? {
    while (hasNext()) {
        if (next() == element) {
            if (hasNext()) {
                return next()
            }
        }
    }

    return null
}
