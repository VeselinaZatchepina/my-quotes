package com.github.veselinazatchepina.myquotes.abstracts

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.OvershootInterpolator
import com.github.veselinazatchepina.myquotes.R
import com.github.veselinazatchepina.myquotes.addquote.AddQuoteActivity
import com.github.veselinazatchepina.myquotes.enums.QuoteType
import com.github.veselinazatchepina.myquotes.setFirstVowelColor
import icepick.Icepick
import kotlinx.android.synthetic.main.activity_single_fragment.*
import kotlinx.android.synthetic.main.fab_popup_menu.*


abstract class SingleFragmentAbstractActivity : AppCompatActivity() {

    private lateinit var fabOpenAnimation: Animation
    private lateinit var fabCloseAnimation: Animation
    private var isFabOpen = false

    companion object {
        const val FRAGMENT_TAG = "fragment_tag"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        Icepick.restoreInstanceState(this, savedInstanceState)
        setContentView(getLayoutResId())
        defineInputData()
        defineToolbar()
        defineNavigationDrawer()
        defineAppBarLayoutExpandableValue()
        defineFab()
        if (title != null) {
            setNewTitleStyle(title.toString())
        }
        defineFragment()
        createPresenter()
    }

    open fun getLayoutResId(): Int = R.layout.activity_current_single_fragment

    open fun defineInputData() {}

    private fun defineToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    open fun defineNavigationDrawer() {}

    open fun defineAppBarLayoutExpandableValue() {
        setAppBarNotExpandable()
    }

    fun setAppBarNotExpandable() {
        appbarLayout.setExpanded(false, false)
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            appbarLayout.layoutParams.height = this.resources
                    .getDimension(R.dimen.toolbar_height_normal_portrait).toInt()
        } else {
            appbarLayout.layoutParams.height = this.resources
                    .getDimension(R.dimen.toolbar_height_normal_landscape).toInt()
        }
        collapsingToolbar.isTitleEnabled = false
    }

    private fun defineFab() {
        defineFabAnimation()
        val fabImageResourceId = setFabImageResId()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            addFab.setImageDrawable(resources.getDrawable(fabImageResourceId, theme))
        } else {
            addFab.setImageDrawable(resources.getDrawable(fabImageResourceId))
        }
        defineActionWhenFabIsPressed()
    }

    private fun defineFabAnimation() {
        fabOpenAnimation = AnimationUtils.loadAnimation(this, R.anim.open_fab_menu)
        fabCloseAnimation = AnimationUtils.loadAnimation(this, R.anim.close_fab_menu)
    }

    open fun setFabImageResId(): Int = R.drawable.ic_add_white_24dp

    open fun defineActionWhenFabIsPressed() {
        addFab.setOnClickListener {
            defineBookFabListener()
            defineMyQuoteFabListener()
            if (!isFabOpen) {
                showFabMenu()
            } else {
                closeFabMenu()
            }
        }
    }

    private fun defineBookFabListener() {
        bookQuoteFab.setOnClickListener {
            startActivity(AddQuoteActivity.newIntent(this, getString(QuoteType.BOOK_QUOTE.resource)))
            closeFabMenu()
        }
    }

    private fun defineMyQuoteFabListener() {
        myQuoteFab.setOnClickListener {
            startActivity(AddQuoteActivity.newIntent(this, getString(QuoteType.MY_QUOTE.resource)))
            closeFabMenu()
        }
    }

    private fun showFabMenu() {
        addFab.animate().rotation(45.0F)
                .withLayer()
                .setDuration(300)
                .setInterpolator(OvershootInterpolator(10.0F))
                .start()
        bookQuoteFab.startAnimation(fabOpenAnimation)
        myQuoteFab.startAnimation(fabOpenAnimation)
        isFabOpen = true
    }

    private fun closeFabMenu() {
        addFab.animate().rotation(0.0F)
                .withLayer()
                .setDuration(300)
                .setInterpolator(OvershootInterpolator(10.0F))
                .start()
        bookQuoteFab.startAnimation(fabCloseAnimation)
        myQuoteFab.startAnimation(fabCloseAnimation)
        isFabOpen = false
    }

    open fun setNewTitleStyle(title: String) {
        setTitle(title.setFirstVowelColor(this))
    }

    private fun defineFragment() {
        var currentFragment = supportFragmentManager.findFragmentById(R.id.container)
        if (currentFragment == null) {
            currentFragment = createFragment()
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, currentFragment, FRAGMENT_TAG)
                    .commit()
        }
    }

    abstract fun createFragment(): Fragment

    abstract fun createPresenter()

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Icepick.saveInstanceState(this, outState)
    }
}