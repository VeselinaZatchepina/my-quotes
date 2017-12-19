package com.github.veselinazatchepina.myquotes.abstracts

import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.github.veselinazatchepina.myquotes.R
import com.github.veselinazatchepina.myquotes.utils.BaseSchedulerProvider
import com.github.veselinazatchepina.myquotes.utils.SchedulerProvider
import kotlinx.android.synthetic.main.activity_single_fragment.*
import org.jetbrains.anko.toast


abstract class SingleFragmentAbstractActivity : AppCompatActivity() {

    companion object {
        fun provideSchedulerProvider(): BaseSchedulerProvider {
            return SchedulerProvider.getInstance()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        defineInputData()
        setContentView(getLayoutResId())
        defineToolbar()
        setAppBarNotExpandable()
        setNewTitleStyle(title.toString())
        defineFragment()
        defineFab()
    }

    open fun defineInputData() { }

    open fun getLayoutResId() : Int = R.layout.activity_current_single_fragment

    private fun defineToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setAppBarNotExpandable() {

    }

    fun setNewTitleStyle(title: String) {

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

}