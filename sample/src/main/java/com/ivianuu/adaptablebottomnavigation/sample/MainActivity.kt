package com.ivianuu.adaptablebottomnavigation.sample

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.ivianuu.adaptablebottomnavigation.BottomNavigationAdapter
import kotlinx.android.synthetic.main.fragment_bottom_navigation.*
import kotlinx.android.synthetic.main.fragment_sample.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.root_container, BottomNavigationFragment())
                .commit()
        }
    }
}

class BottomNavigationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_navigation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = BottomNavAdapter(childFragmentManager)
        bottom_bar.setAdapter(adapter)

        bottom_bar.setOnNavigationItemSelectedListener {
            Log.d("Clicks", it.toString())
            true
        }
    }

}

class BottomNavAdapter(fm: FragmentManager) : BottomNavigationAdapter(
    fm, R.id.bottom_navigation_container) {

    override fun createFragment(item: MenuItem): Fragment {
        return when(item.itemId) {
            R.id.one -> FragmentOne()
            R.id.two -> FragmentTwo()
            R.id.three -> FragmentThree()
            else -> throw IllegalArgumentException("unknown item $item")
        }
    }

    override fun getFragmentTag(item: MenuItem): String {
        return item.title.toString()
    }
}

abstract class PageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sample, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        page.text = title
        other.setOnClickListener {
            activity?.let {
                it.supportFragmentManager.beginTransaction()
                    .replace(R.id.root_container, FragmentOther())
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    abstract val title: String
}

class FragmentOne : PageFragment() {
    override val title: String = "One"
}

class FragmentTwo : PageFragment() {
    override val title: String = "Two"
}

class FragmentThree : PageFragment() {
    override val title: String = "Three"
}

class FragmentOther : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_other, container, false)
    }

}