package com.github.veselinazatchepina.myquotes.getidea

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.view.View
import com.github.veselinazatchepina.myquotes.R
import com.github.veselinazatchepina.myquotes.abstracts.NavigationDrawerAbstractActivity
import com.github.veselinazatchepina.myquotes.data.QuoteRepository
import com.github.veselinazatchepina.myquotes.data.local.AppDatabase
import com.github.veselinazatchepina.myquotes.data.local.QuoteLocalDataSource
import com.github.veselinazatchepina.myquotes.data.remote.QuoteRemoteDataSource
import kotlinx.android.synthetic.main.fab_popup_menu.*


class GetIdeaActivity : NavigationDrawerAbstractActivity() {

    private var getIdeaView: GetIdeaFragment? = null
    private var getIdeaPresenter: GetIdeaPresenter? = null

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, GetIdeaActivity::class.java)
        }
    }

    override fun defineInputData() {
        addFab.visibility = View.GONE
        title = getString(R.string.idea_title)
    }

    override fun createFragment(): Fragment {
        getIdeaView = GetIdeaFragment.createInstance()
        return getIdeaView!!
    }

    override fun createPresenter() {
        val quoteRepository = QuoteRepository.getInstance(
                QuoteLocalDataSource.getInstance(AppDatabase.getAppDatabaseInstance(applicationContext)),
                QuoteRemoteDataSource.getInstance())
        getIdeaPresenter = GetIdeaPresenter(quoteRepository,
                getIdeaView
                        ?: supportFragmentManager.findFragmentByTag(FRAGMENT_TAG) as GetIdeaFragment)
    }
}