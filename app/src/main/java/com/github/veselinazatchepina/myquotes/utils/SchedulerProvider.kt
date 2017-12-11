package com.github.veselinazatchepina.myquotes.utils

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class SchedulerProvider : BaseSchedulerProvider {

    companion object {
        private var INSTANCE: SchedulerProvider? = null

        fun getInstance(): SchedulerProvider {
            if (INSTANCE == null) {
                INSTANCE = SchedulerProvider()
            }
            return INSTANCE!!
        }
    }

    override fun computation(): Scheduler {
        return Schedulers.computation()
    }

    override fun io(): Scheduler {
        return Schedulers.io()
    }

    override fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}