/*
 * Copyright 2018 Manuel Wrage
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ivianuu.adaptablebottomnavigation

import android.annotation.SuppressLint
import android.content.Context
import android.support.design.widget.BottomNavigationView
import android.util.AttributeSet
import android.os.Parcelable
import android.support.v4.view.ViewPager
import android.view.MenuItem
import kotlinx.android.parcel.Parcelize

/**
 * A [BottomNavigationView] which can be used with a [ViewPager]
 */
class AdaptableBottomNavigationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BottomNavigationView(context, attrs, defStyleAttr) {

    private var currentViewSwapperSelectedListener: ViewSwapperOnItemSelectedListener? = null
    private var viewChangeListener: BottomNavigationView.OnNavigationItemSelectedListener? = null
    private var selectedPosition = 0

    override fun setOnNavigationItemSelectedListener(
        listener: BottomNavigationView.OnNavigationItemSelectedListener?
    ) {
        viewChangeListener = listener
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        return SavedState(selectedPosition, superState)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }

        super.onRestoreInstanceState(state.superState)

        selectedPosition = state.selectedPosition
        menu.getItem(selectedPosition).isChecked = true
    }

    fun setupWithViewPager(viewPager: ViewPager?) {
        currentViewSwapperSelectedListener = null

        if (viewPager != null) {
            currentViewSwapperSelectedListener = ViewSwapperOnItemSelectedListener(viewPager)
            super.setOnNavigationItemSelectedListener(currentViewSwapperSelectedListener)
        }
    }

    private inner class ViewSwapperOnItemSelectedListener(
        private val viewPager: ViewPager
    ) : BottomNavigationView.OnNavigationItemSelectedListener {

        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            for (i in 0 until menu.size()) {
                if (menu.getItem(i).itemId == item.itemId) {
                    selectedPosition = i
                    viewPager.currentItem = selectedPosition
                    break
                }
            }

            viewChangeListener?.onNavigationItemSelected(item)

            return true
        }
    }

    @SuppressLint("ParcelCreator")
    @Parcelize
    private class SavedState(
        val selectedPosition: Int,
        val superState: Parcelable?
    ) : Parcelable
}