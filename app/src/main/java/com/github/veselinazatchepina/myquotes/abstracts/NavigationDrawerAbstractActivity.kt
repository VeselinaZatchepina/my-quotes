package com.github.veselinazatchepina.myquotes.abstracts

import android.content.Context
import android.content.Intent
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.view.Surface
import android.view.WindowManager
import com.github.veselinazatchepina.myquotes.R
import com.github.veselinazatchepina.myquotes.allquote.AllQuotesActivity
import com.github.veselinazatchepina.myquotes.enums.QuoteType
import com.github.veselinazatchepina.myquotes.quotecategories.QuoteCategoriesActivity
import kotlinx.android.synthetic.main.activity_nav_drawer.*
import kotlinx.android.synthetic.main.activity_single_fragment.*


abstract class NavigationDrawerAbstractActivity : SingleFragmentAbstractActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun getLayoutResId(): Int = R.layout.activity_nav_drawer

    override fun defineNavigationDrawer() {
        val toggle = ActionBarDrawerToggle(this,
                drawer_layout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close)
        drawer_layout.setDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun defineAppBarLayoutExpandableValue() {
        defineToolbarForScreenOrientation(this)
    }

    private fun defineToolbarForScreenOrientation(context: Context) {
        val screenOrientation = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
                .defaultDisplay.orientation
        when (screenOrientation) {
            Surface.ROTATION_0 -> "android portrait screen"
            Surface.ROTATION_90 -> {
                setAppBarNotExpandable()
                "android landscape screen"
            }
            Surface.ROTATION_180 -> "android reverse portrait screen"
            else -> {
                setAppBarNotExpandable()
                "android reverse landscape screen"
            }
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var intent: Intent? = null
        when (item.itemId) {
            R.id.nav_book_quote -> intent = QuoteCategoriesActivity.newIntent(this, getString(QuoteType.BOOK_QUOTE.resource))
            R.id.nav_my_quote -> intent = QuoteCategoriesActivity.newIntent(this, getString(QuoteType.MY_QUOTE.resource))
            R.id.nav_all_quotes -> intent = AllQuotesActivity.newIntent(this)
            R.id.nav_get_idea -> intent = null
        }
        if (intent != null) {
            startActivity(intent)
        }
        return true
    }
}
