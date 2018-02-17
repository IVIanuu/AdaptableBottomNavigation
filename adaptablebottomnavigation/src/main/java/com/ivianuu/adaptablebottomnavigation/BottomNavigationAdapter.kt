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

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.MenuItem

/**
 * Adapter for [AdaptableBottomNavigationView]'s
 */
abstract class BottomNavigationAdapter(
    private val fm: FragmentManager,
    private val containerId: Int,
    private val mode: Mode = Mode.REPLACE
)
{

    private var currentFragmentTag: String? = null

    fun setCurrentItem(item: MenuItem) {
        val newFragmentTag = getFragmentTag(item)

        val transaction = fm.beginTransaction()
        when(mode) {
            Mode.REPLACE -> {
                d { "swap fragments" }
                // just swap the current fragment with a new one
                val fragment = createFragment(item)
                transaction.replace(containerId, fragment, newFragmentTag)
            }
            Mode.ATTACH_DETACH -> {
                val oldFragment: Fragment? = fm.findFragmentById(containerId)

                // detach old fragment
                if (oldFragment != null) {
                    d { "detach old fragment ${oldFragment.tag}" }
                    transaction.detach(oldFragment)
                }

                var newFragment = fm.findFragmentByTag(newFragmentTag)
                if (newFragment != null) {
                    // re attach new fragment
                    d { "attach new fragment ${newFragment.tag}" }
                    transaction.attach(newFragment)
                } else {
                    newFragment = createFragment(item)
                    d { "add new fragment $newFragment" }
                    // add new fragment for the first time
                    transaction.add(containerId, newFragment, newFragmentTag)
                }
            }
            Mode.SHOW_HIDE -> {
                // hide all fragments except the new one
                fm.fragments
                    .filter { it.tag != newFragmentTag }
                    .forEach {
                        d { "hide old fragment ${it.tag}" }
                        transaction.hide(it)
                    }

                var newFragment = fm.findFragmentByTag(newFragmentTag)
                if (newFragment != null) {
                    // show new fragment
                    d { "show new fragment ${newFragment.tag}" }
                    transaction.show(newFragment)
                } else {
                    newFragment = createFragment(item)
                    d { "add new fragment $newFragment" }
                    // add new fragment for the first time
                    transaction.add(containerId, newFragment, newFragmentTag)
                }
            }
        }

        transaction.commitNowAllowingStateLoss()
        currentFragmentTag = newFragmentTag
    }

    fun getCurrentFragment(): Fragment? {
        return fm.findFragmentByTag(currentFragmentTag)
    }

    protected abstract fun createFragment(item: MenuItem): Fragment

    protected open fun getFragmentTag(item: MenuItem): String {
        return item.itemId.toString()
    }

    enum class Mode {
        REPLACE, ATTACH_DETACH, SHOW_HIDE
    }
}