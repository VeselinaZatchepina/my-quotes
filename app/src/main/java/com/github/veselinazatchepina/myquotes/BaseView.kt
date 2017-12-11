package com.github.veselinazatchepina.myquotes


interface BaseView<T> {

    fun setPresenter(presenter: T)
}