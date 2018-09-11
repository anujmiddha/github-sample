package com.moldedbits.githubsample.view

/**
 * Abstract Event from ViewModel
 */
open class State

/**
 * Generic loading Event
 */
object LoadingState : State()

/**
 * Generic Error State
 * @param error - caught error
 */
data class ErrorState(val error: Throwable) : State()