package com.github.veselinazatchepina.myquotes.abstracts

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.github.veselinazatchepina.myquotes.R
import com.github.veselinazatchepina.myquotes.enums.QuoteType
import com.github.veselinazatchepina.myquotes.quotecategories.BookCategoriesActivity
import com.github.veselinazatchepina.myquotes.utils.BaseSchedulerProvider
import com.github.veselinazatchepina.myquotes.utils.SchedulerProvider
import kotlinx.android.synthetic.main.activity_nav_drawer.*
import kotlinx.android.synthetic.main.activity_single_fragment.*
import org.jetbrains.anko.toast


abstract class NavigationDrawerAbstractActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        fun provideSchedulerProvider(): BaseSchedulerProvider {
            return SchedulerProvider.getInstance()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
        defineNavigationDrawer()
        defineInputData()
        defineFragment()
        createPresenter()
        defineFab()
    }

    abstract fun getLayoutResId() : Int

    private fun defineNavigationDrawer() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val toggle = ActionBarDrawerToggle(this,
                drawer_layout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close)
        drawer_layout.setDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
    }

    private fun defineFragment() {
        var currentFragment = supportFragmentManager.findFragmentById(R.id.container)
        if (currentFragment == null) {
            currentFragment = createFragment()
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, currentFragment)
                    .commit()
        }
    }

    abstract fun createFragment(): Fragment

    abstract fun createPresenter()

    open fun defineInputData() {

    }

    private fun defineFab() {
        val fabImageResourceId = setFabImageResId()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fab.setImageDrawable(resources.getDrawable(fabImageResourceId, theme))
        } else{
            fab.setImageDrawable(resources.getDrawable(fabImageResourceId))
        }
    }

    open fun setFabImageResId() : Int = R.drawable.ic_add_white_24dp

    open fun defineActionWhenFabIsPressed(view: View) {
        toast("Hi")
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
