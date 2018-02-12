package com.github.veselinazatchepina.myquotes

import android.arch.persistence.room.Room
import android.content.Context
import com.github.veselinazatchepina.myquotes.data.local.AppDatabase
import com.github.veselinazatchepina.myquotes.data.local.QuoteLocalDataSource
import com.github.veselinazatchepina.myquotes.quotecategories.QuoteCategoriesContract
import io.reactivex.subscribers.TestSubscriber
import org.junit.Before
import org.mockito.Mockito


class QuoteCategoriesPresenterTest {

    lateinit var context: Context
    lateinit var roomDatabase: AppDatabase
    lateinit var dataSource: QuoteLocalDataSource
    lateinit var quoteCategoriesView: QuoteCategoriesContract.View
    lateinit var testSubscriber: TestSubscriber<List<String>>

    @Before
    fun init() {
        context = Mockito.mock(Context::class.java)
        roomDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        dataSource = QuoteLocalDataSource.getInstance(roomDatabase)
        testSubscriber = TestSubscriber<List<String>>()
    }



}