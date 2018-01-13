package com.github.veselinazatchepina.myquotes.abstracts

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.OvershootInterpolator
import com.github.veselinazatchepina.myquotes.R
import com.github.veselinazatchepina.myquotes.addquote.AddQuoteActivity
import com.github.veselinazatchepina.myquotes.enums.QuoteType
import com.github.veselinazatchepina.myquotes.utils.BaseSchedulerProvider
import com.github.veselinazatchepina.myquotes.utils.SchedulerProvider
import kotlinx.android.synthetic.main.activity_single_fragment.*
import kotlinx.android.synthetic.main.fab_popup_menu.*


abstract class SingleFragmentAbstractActivity : AppCompatActivity() {

    lateinit var fabOpenAnimation: Animation
    lateinit var fabCloseAnimation: Animation
    var isFabOpen = false

    companion object {
        fun provideSchedulerProvider(): BaseSchedulerProvider {
            return SchedulerProvider.getInstance()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
        defineToolbar()
        defineNavigationDrawer()
        defineAppBarLayoutExpandableValue()
        setNewTitleStyle(title.toString())
        defineFab()
        defineInputData()
        defineFragment()
        createPresenter()
    }

    open fun getLayoutResId(): Int = R.layout.activity_current_single_fragment

    private fun defineToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    open fun defineNavigationDrawer() {}

    open fun defineAppBarLayoutExpandableValue() {
        setAppBarNotExpandable()
    }

    fun setAppBarNotExpandable() {
        appbar_layout.setExpanded(false, false)
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            appbar_layout.layoutParams.height = this.resources.getDimension(R.dimen.toolbar_height_normal_portrait).toInt()
        } else {
            appbar_layout.layoutParams.height = this.resources.getDimension(R.dimen.toolbar_height_normal_landscape).toInt()
        }
        collapsing_toolbar.isTitleEnabled = false
    }

    fun setNewTitleStyle(title: String) {}

    private fun defineFabAnimation() {
        fabOpenAnimation = AnimationUtils.loadAnimation(this, R.anim.open_fab_menu)
        fabCloseAnimation = AnimationUtils.loadAnimation(this, R.anim.close_fab_menu)
    }

    private fun defineFab() {
        defineFabAnimation()
        val fabImageResourceId = setFabImageResId()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            add_icon_fab.setImageDrawable(resources.getDrawable(fabImageResourceId, theme))
        } else {
            add_icon_fab.setImageDrawable(resources.getDrawable(fabImageResourceId))
        }
    }

    open fun setFabImageResId(): Int = R.drawable.ic_add_white_24dp

    open fun defineActionWhenFabIsPressed(view: View) {
        defineBookFabListener()
        defineMyQuoteFabListener()
        if (!isFabOpen) {
            showFabMenu()
        } else {
            closeFabMenu()
        }
    }

    private fun defineBookFabListener() {
        book_quote_fab.setOnClickListener {
            startActivity(AddQuoteActivity.newIntent(this, getString(QuoteType.BOOK_QUOTE.resource)))
            closeFabMenu()
        }
    }

    private fun defineMyQuoteFabListener() {
        my_quote_fab.setOnClickListener {
            startActivity(AddQuoteActivity.newIntent(this, getString(QuoteType.MY_QUOTE.resource)))
            closeFabMenu()
        }
    }

    private fun showFabMenu() {
        add_icon_fab.animate().rotation(45.0F).withLayer().setDuration(300).setInterpolator(OvershootInterpolator(10.0F)).start()
        book_quote_fab.startAnimation(fabOpenAnimation)
        my_quote_fab.startAnimation(fabOpenAnimation)
        isFabOpen = true
    }

    private fun closeFabMenu() {
        add_icon_fab.animate().rotation(0.0F).withLayer().setDuration(300).setInterpolator(OvershootInterpolator(10.0F)).start()
        book_quote_fab.startAnimation(fabCloseAnimation)
        my_quote_fab.startAnimation(fabCloseAnimation)
        isFabOpen = false
    }

    open fun defineInputData() {}

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
}