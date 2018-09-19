/*
 *  Copyright 2011 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package cn.magicwindow.wheelview.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.util.*

/**
 * Numeric Wheel adapter.
 *
 * @author aaron
 */
class NumericWheelAdapter(context: Context, private val minValue: Int, private val maxValue: Int, private val currentValue: Int = -1, private val format: String? = null) : AbstractWheelTextAdapter(context) {

    private var label: String? = null
    /**
     * get the list of show textview
     *
     * @return the array of textview
     */
    val testViews = ArrayList<View>()

    // 当前Index
    private var currentItem: Int = 0


    public override fun getItemText(index: Int): CharSequence? {
        if (index in 0 until getItemsCount()) {
            val value = minValue + index
            return if (format != null) String.format(format, value) else Integer.toString(value)
        }
        return null
    }

    override fun getItemsCount(): Int {
        return maxValue - minValue + 1
    }

    @SuppressLint("SetTextI18n")
    override fun getItem(index: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        currentItem = index
        if (index in 0 until getItemsCount()) {
            if (convertView == null) {
                convertView = getView(itemResource, parent)
            }
            val textView = getTextView(convertView, itemTextResource)
            textView?.let {
                if (!testViews.contains(it)) {
                    testViews.add(it)
                }
            }

            if (textView != null) {
                var text = getItemText(index)
                if (text == null) {
                    text = ""
                }
                if (TextUtils.isEmpty(label)) {
                    textView.text = text
                } else {
                    textView.text = text.toString() + label!!
                }

                if (itemResource == AbstractWheelTextAdapter.TEXT_VIEW_ITEM_RESOURCE) {
                    configureTextView(textView)
                }
            }
            return convertView
        }
        return null
    }

    override fun configureTextView(view: TextView?) {
        super.configureTextView(view)
        if (currentItem == currentValue) {
            view?.setTextColor(selectTextColor)
            view?.textSize = selectTextSize.toFloat()
            view?.setTypeface(Typeface.DEFAULT, selectTextStyle)
        }
        view?.typeface = Typeface.SANS_SERIF
    }

    fun setLabel(label: String) {
        this.label = label
    }

    companion object {

        /**
         * The default min value
         */
        val DEFAULT_MAX_VALUE = 9

        /**
         * The default max value
         */
        private val DEFAULT_MIN_VALUE = 0
    }

}
