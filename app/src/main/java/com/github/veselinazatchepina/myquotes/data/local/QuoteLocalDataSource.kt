package com.github.veselinazatchepina.myquotes.data.local

import android.annotation.SuppressLint
import android.content.Context
import com.github.veselinazatchepina.myquotes.data.QuoteDataSource
import com.github.veselinazatchepina.myquotes.utils.BaseSchedulerProvider


class QuoteLocalDataSource private constructor(val context: Context,
                                               val schedulerProvider: BaseSchedulerProvider) : QuoteDataSource {

    private var databaseInstance: AppDatabase? = null

    init {
        databaseInstance = AppDatabase.getAppDatabaseInstance(context)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var INSTANCE: QuoteLocalDataSource? = null

        fun getInstance(context: Context,
                        schedulerProvider: BaseSchedulerProvider): QuoteLocalDataSource {
            if (INSTANCE == null) {
                INSTANCE = QuoteLocalDataSource(context.applicationContext, schedulerProvider)
            }
            return INSTANCE!!
        }
    }
}