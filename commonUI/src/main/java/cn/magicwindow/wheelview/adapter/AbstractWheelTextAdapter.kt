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

import android.content.Context
import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Abstract wheel adapter provides common functionality for adapters.
 */
abstract class AbstractWheelTextAdapter @JvmOverloads protected constructor(// Current context
        protected var context: Context, // Items resources

        var itemResource: Int = TEXT_VIEW_ITEM_RESOURCE,

        var itemTextResource: Int = NO_RESOURCE) : AbstractWheelAdapter() {
    // Layout inflater
    protected var inflater: LayoutInflater
    // Empty items resources
    /**
     * Gets resource Id for empty items views
     *
     * @return the empty item resource Id
     */
    /**
     * Sets resource Id for empty items views
     *
     * @param emptyItemResourceId the empty item resource Id to set
     */
    var emptyItemResource: Int = 0
// Text settings
    /**
     * Gets text color
     *
     * @return the text color
     */
    /**
     * Sets text color
     *
     * @param textColor the text color to set
     */
     var textColor = DEFAULT_TEXT_COLOR
    /**
     * Gets text size
     *
     * @return the text size
     */
    /**
     * Sets text size
     *
     * @param textSize the text size to set
     */
     var textSize = DEFAULT_TEXT_SIZE
    /**
     * Gets text size
     *
     * @return the text size
     */
    /**
     * Sets text size
     *
     * @param textSize the text size to set
     */
     var selectTextSize = DEFAULT_TEXT_SIZE
     var selectTextStyle = Typeface.BOLD
     var selectTextColor = DEFAULT_TEXT_COLOR

    class Builder

    init {

        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }


    /**
     * Returns text for specified item
     *
     * @param index the item index
     * @return the text of specified items
     */
    protected abstract fun getItemText(index: Int): CharSequence?

    override fun getItem(index: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        if (index >= 0 && index < getItemsCount()) {
            if (convertView == null) {
                convertView = getView(itemResource, parent)
            }
            val textView = getTextView(convertView, itemTextResource)
            if (textView != null) {
                var text: CharSequence? = getItemText(index)
                if (text == null) {
                    text = ""
                }
                textView.text = text

                if (itemResource == TEXT_VIEW_ITEM_RESOURCE) {
                    configureTextView(textView)
                }
            }
            return convertView
        }
        return null
    }

    override fun getEmptyItem(convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        if (convertView == null) {
            convertView = getView(emptyItemResource, parent)
        }
        if (emptyItemResource == TEXT_VIEW_ITEM_RESOURCE && convertView is TextView) {
            configureTextView(convertView as TextView?)
        }

        return convertView
    }

    /**
     * Configures text view. Is called for the TEXT_VIEW_ITEM_RESOURCE views.
     *
     * @param view the text view to be configured
     */
    protected open fun configureTextView(view: TextView?) {
        view?.setTextColor(textColor)
        view?.gravity = Gravity.CENTER
        view?.textSize = textSize.toFloat()
        view?.setLines(1)
        //        view.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
    }

    /**
     * Loads a text view from view
     *
     * @param view         the text view or layout containing it
     * @param textResource the text resource Id in layout
     * @return the loaded text view
     */
    fun getTextView(view: View?, textResource: Int): TextView? {
        var text: TextView? = null
        try {
            if (textResource == NO_RESOURCE && view is TextView) {
                text = view
            } else if (textResource != NO_RESOURCE) {
                text = view?.findViewById(textResource)
            }
        } catch (e: ClassCastException) {
            throw IllegalStateException(
                    "AbstractWheelAdapter requires the resource ID to be a TextView", e)
        }

        return text
    }

    /**
     * Loads view from resources
     *
     * @param resource the resource Id
     * @return the loaded view or null if resource is not set
     */
    fun getView(resource: Int, parent: ViewGroup): View? {
        when (resource) {
            NO_RESOURCE -> return null
            TEXT_VIEW_ITEM_RESOURCE -> {
                val textView = TextView(context)
                textView.setPadding(0, 20, 0, 20)
                return textView
            }
            else -> return inflater.inflate(resource, parent, false)
        }
    }

    companion object {

        /**
         * Text view resource. Used as a default view for adapter.
         */
        val TEXT_VIEW_ITEM_RESOURCE = -1
        /**
         * Default text color
         */
        val DEFAULT_TEXT_COLOR = -0xa7a7a8
        /**
         * Default text color
         */
        val LABEL_COLOR = -0x8fff90
        /**
         * Default text size
         */
        val DEFAULT_TEXT_SIZE = 18
        /**
         * No resource constant.
         */
        protected val NO_RESOURCE = 0
    }
}
/**
 * Constructor
 *
 * @param context the current context
 */
/**
 * Constructor
 *
 * @param context      the current context
 * @param itemResource the resource ID for a layout file containing a TextView to use when instantiating items views
 */
