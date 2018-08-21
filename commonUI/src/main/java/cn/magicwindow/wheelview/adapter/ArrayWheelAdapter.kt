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
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * The simple Array wheel adapter
 *
 * @param <T> the element type
 * @author aaron
</T> */
class ArrayWheelAdapter<T>(context: Context, private val items: Array<T>?, private val currentValue: Int = -1)
    : AbstractWheelTextAdapter(context) {

    // 当前Index
    private var currentItem: Int = 0

    public override fun getItemText(index: Int): CharSequence? {
        if (items != null && index >= 0 && index < items.size) {
            val item = items[index]
            return if (item is CharSequence) {
                item
            } else item.toString()
        }
        return null
    }

    override fun configureTextView(view: TextView?) {
        super.configureTextView(view)
//        Log.e("aaron", "currentItem:$currentItem,currentValue:$currentValue,textSize:$textSize,selectTextSize:$selectTextSize")
        if (currentItem == currentValue) {
            view?.setTextColor(selectTextColor)
            view?.textSize = selectTextSize.toFloat()
            view?.setTypeface(Typeface.DEFAULT, selectTextStyle)
        }
        view?.typeface = Typeface.SANS_SERIF
    }

    override fun getItem(index: Int, convertView: View?, parent: ViewGroup): View? {
        currentItem = index
        return super.getItem(index, convertView, parent)
    }

    override fun getItemsCount(): Int {
        return items?.size ?: 0
    }
}
