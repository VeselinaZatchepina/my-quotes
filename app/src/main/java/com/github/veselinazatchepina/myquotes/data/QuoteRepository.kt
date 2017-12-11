package com.github.veselinazatchepina.myquotes.data


class QuoteRepository private constructor(val quoteLocalDataSource: QuoteDataSource,
                                          val quoteRemoteDataSource: QuoteDataSource) : QuoteDataSource {

    companion object {
        private var INSTANCE: QuoteRepository? = null

        fun getInstance(quoteLocalDataSource: QuoteDataSource,
                        quoteRemoteDataSource: QuoteDataSource): QuoteRepository {
            if (INSTANCE == null) {
                INSTANCE = QuoteRepository(quoteLocalDataSource, quoteRemoteDataSource)
            }
            return INSTANCE!!
        }
    }


}