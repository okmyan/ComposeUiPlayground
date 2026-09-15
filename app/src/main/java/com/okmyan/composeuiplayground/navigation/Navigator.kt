package com.okmyan.composeuiplayground.navigation

import androidx.navigation3.runtime.NavKey

/**
 * Handles navigation events (forward and back) by updating the navigation state
 */
class Navigator<T : NavKey>(val state: NavigationState<T>) {

    /**
     * Navigates to a new route.
     * If [route] is a top-level route, it switches to it. If the top-level route is already active,
     * it resets its back stack
     * If [unique] is true, it ensures only one instance of the route exists in the current stack
     * by popping back to the existing instance if found.
     */
    // TODO remove unique parameter
    fun navigate(route: T, unique: Boolean = false) {
        if (route in state.backStacks.keys) {
            if (state.topLevelRoute == route) {
                resetStack(route)
            } else {
                state.topLevelRoute = route
            }
            return
        }

        val backStack = state.backStacks[state.topLevelRoute] ?: return
        if (unique) {
            val index = backStack.indexOf(route)
            if (index != -1) {
                // Clear the existing instance and everything after it
                backStack.subList(index, backStack.size).clear()
            }
        }
        backStack.add(route)
    }

    fun replaceCurrent(route: T) {
        if (route in state.backStacks.keys) {
            if (state.topLevelRoute == route) {
                resetStack(route)
            } else {
                state.topLevelRoute = route
            }
            return
        }

        val backStack = state.backStacks[state.topLevelRoute] ?: return

        if (backStack.size > 1) {
            backStack[backStack.lastIndex] = route
        } else {
            backStack.add(route)
        }
    }

    /**
     * Goes back in the current stack.
     * If at the root of a non-start top-level route, switches to the start route.
     * Returns true if the back event was handled, false otherwise
     */
    fun goBack(): Boolean {
        val currentStack = state.backStacks[state.topLevelRoute] ?: return false
        val currentRoute = currentStack.lastOrNull() ?: return false

        return if (currentRoute == state.topLevelRoute) {
            if (state.topLevelRoute != state.startRoute) {
                state.topLevelRoute = state.startRoute
                true
            } else {
                false
            }
        } else {
            currentStack.removeLastOrNull()
            true
        }
    }

    /**
     * Resets the back stack for the given [route] (defaults to current) to its root element
     */
    fun resetStack(route: T = state.topLevelRoute) {
        val backStack = state.backStacks[route] ?: return
        if (backStack.size > 1) {
            // Remove everything except the root element (index 0) in one operation
            backStack.subList(1, backStack.size).clear()
        }
    }

    fun isRouteOnTopOfBackStack(route: T): Boolean {
        val currentStack = state.backStacks[state.topLevelRoute]
        return currentStack?.lastOrNull() == route
    }
}
