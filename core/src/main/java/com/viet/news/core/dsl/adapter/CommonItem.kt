package com.viet.news.core.dsl.adapter

import android.support.annotation.LayoutRes
import android.view.View
import android.view.ViewGroup

/**
 *@version:
 *@FileDescription:
 *@Author:jing
 *@Since:2018/6/13
 *@ChangeList:
 */
abstract class CommonItem<T> (var click: ((d: T, pos: Int) -> Unit)? = null,
                                                  var doubleTap: ((d: T, pos: Int) -> Unit)? = null,
                                                  var longPress: ((d: T, pos: Int) -> Unit)? = null) {

    @LayoutRes
    abstract fun getResId(): Int

    open fun onViewCreate(parent: ViewGroup, view: View) {
    }

    abstract fun showItem(t: T, pos: Int, view: View)

    open fun isMeetData(t: T, pos: Int): Boolean = true

    open fun showItem(t: T, pos: Int, view: View, payloads: MutableList<Any>) {

    }

    fun hasEvent() = click != null || doubleTap != null || longPress != null
}

class CommonAdapterDSL<T> constructor(private inline var create: (parent: ViewGroup, view: View) -> Unit = { _, _ -> Unit },

                                      private inline var dataBind: (t: T, pos: Int, view: View) -> Unit = { _: T, _: Int, _: View -> Unit },

                                      private inline var dataPayload: (t: T, pos: Int, view: View, p: MutableList<Any>) -> Unit = { _: T, _: Int, _: View, _: MutableList<Any> -> Unit },

                                      private inline var dataMeet: (t: T, pos: Int) -> Boolean = { _: T, _: Int -> false },

                                      private inline var click: ((t: T, pos: Int) -> Unit)? = null,

                                      private inline var doubleTap: ((t: T, pos: Int) -> Unit)? = null,

                                      private inline var longP: ((t: T, pos: Int) -> Unit)? = null) {

    @LayoutRes
    private var resId: Int = -1

    fun resId(@LayoutRes resId: Int) {
        this.resId = resId
    }

    fun onViewCreate(create: (parent: ViewGroup, view: View) -> Unit) {
        this.create = create
    }

    fun showItem(dataBind: (t: T, pos: Int, view: View) -> Unit) {
        this.dataBind = dataBind
    }

    fun isMeetData(dataMeet: (t: T, pos: Int) -> Boolean) {
        this.dataMeet = dataMeet
    }

    fun showItemPayload(dataPayload: (t: T, pos: Int, view: View, payloads: MutableList<Any>) -> Unit) {
        this.dataPayload = dataPayload
    }

    fun onClick(event: (t: T, pos: Int) -> Unit) {
        this.click = event
    }

    fun onDoubleTap(dT: (t: T, pos: Int) -> Unit) {
        this.doubleTap = dT
    }

    fun longPress(lp: (t: T, pos: Int) -> Unit) {
        this.longP = lp
    }

    internal fun build(): CommonItem<T> {
        return object : CommonItem<T>(this.click, this.doubleTap, this.longP) {
            override fun isMeetData(t: T, pos: Int): Boolean = dataMeet(t, pos)

            override fun onViewCreate(parent: ViewGroup, view: View) {
                super.onViewCreate(parent, view)
                create(parent, view)
            }

            override fun getResId(): Int = resId

            override fun showItem(t: T, pos: Int, view: View) {
                dataBind(t, pos, view)
            }

            override fun showItem(t: T, pos: Int, view: View, payloads: MutableList<Any>) {
                super.showItem(t, pos, view, payloads)
                dataPayload(t, pos, view, payloads)
            }
        }
    }

}