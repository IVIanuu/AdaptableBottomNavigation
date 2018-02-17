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
import android.os.Parcelable
import android.support.design.widget.BottomNavigationView
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MenuItem
import kotlinx.android.parcel.Parcelize

/*fun Any.d(message: () -> String) {
    Log.d(this::class.java.simpleName, message())
}*/

/**
 * A [BottomNavigationView] which can be used with a [ViewPager]
 */
class AdaptableBottomNavigationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BottomNavigationView(context, attrs, defStyleAttr) {

    private var currentAdapterSelectedListener: AdapterOnItemSelectedListener? = null
    private var viewChangeListener: BottomNavigationView.OnNavigationItemSelectedListener? = null
    private var selectedItem = 0

    init {
        selectedItem = selectedItemId
    }

    override fun setOnNavigationItemSelectedListener(
        listener: BottomNavigationView.OnNavigationItemSelectedListener?
    ) {
        viewChangeListener = listener
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        return SavedState(selectedItem, superState)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }

        super.onRestoreInstanceState(state.superState)

        selectedItem = state.selectedItem
        currentAdapterSelectedListener?.restore(selectedItem)
    }
    
    fun setAdapter(adapter: BottomNavigationAdapter?) {
        currentAdapterSelectedListener = null

        if (adapter != null) {
            currentAdapterSelectedListener = AdapterOnItemSelectedListener(adapter)
            super.setOnNavigationItemSelectedListener(currentAdapterSelectedListener)
        }
    }

    private inner class AdapterOnItemSelectedListener(
        private val adapter: BottomNavigationAdapter
    ) : BottomNavigationView.OnNavigationItemSelectedListener {

        init {
            restore(selectedItem)
        }

        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            selectedItem = item.itemId
            adapter.setCurrentItem(item)
            viewChangeListener?.onNavigationItemSelected(item)
            return true
        }

        fun restore(selectedItem: Int) {
            // set initial item
            menu.findItem(selectedItem)?.let { adapter.setCurrentItem(it) }
        }
    }

    @SuppressLint("ParcelCreator")
    @Parcelize
    private data class SavedState(
        val selectedItem: Int,
        val superState: Parcelable?
    ) : Parcelable
}