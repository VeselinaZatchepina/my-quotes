package com.github.veselinazatchepina.myquotes.utils

import io.reactivex.Scheduler


interface BaseSchedulerProvider {

    fun computation(): Scheduler

    fun io(): Scheduler

    fun ui(): Scheduler
}