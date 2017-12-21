package com.github.veselinazatchepina.myquotes.abstracts

import android.content.Intent
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import com.github.veselinazatchepina.myquotes.R
import com.github.veselinazatchepina.myquotes.bookcategories.BookCategoriesActivity
import com.github.veselinazatchepina.myquotes.enums.QuoteType
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
            R.id.nav_book_quote -> intent = BookCategoriesActivity.newIntent(this, getString(QuoteType.BOOK_QUOTE.resource))
            R.id.nav_my_quote -> intent = BookCategoriesActivity.newIntent(this, getString(QuoteType.MY_QUOTE.resource))
            R.id.nav_get_idea -> intent = null
        }
        if (intent != null) {
            startActivity(intent)
        }
        return true
    }
}
