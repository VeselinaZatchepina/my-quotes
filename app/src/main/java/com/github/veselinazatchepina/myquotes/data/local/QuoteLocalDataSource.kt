package com.github.veselinazatchepina.myquotes.data.local

import android.annotation.SuppressLint
import android.content.Context
import com.github.veselinazatchepina.myquotes.data.QuoteDataSource
import com.github.veselinazatchepina.myquotes.data.local.entity.BookCategory
import com.github.veselinazatchepina.myquotes.utils.BaseSchedulerProvider
import io.reactivex.Flowable


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

    override fun getBookCategories(): Flowable<List<BookCategory>> {
        return databaseInstance!!.bookCategoryDao().getAllBookCategories()
    }
}