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
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = MainAdapter(supportFragmentManager)
        bottom_bar.setAdapter(adapter)

        bottom_bar.setOnNavigationItemSelectedListener {
            Log.d("Clicks", it.toString())
            true
        }
    }
}

class MainAdapter(fm: FragmentManager) : BottomNavigationAdapter(fm, R.id.container, true) {

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

abstract class BaseFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sample, container, false)
    }

    abstract val title: String
}

class FragmentOne : BaseFragment() {
    override val title: String = "One"
}

class FragmentTwo : BaseFragment() {
    override val title: String = "Two"
}

class FragmentThree : BaseFragment() {
    override val title: String = "Three"
}