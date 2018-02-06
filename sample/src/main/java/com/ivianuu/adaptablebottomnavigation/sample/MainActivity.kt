package com.ivianuu.adaptablebottomnavigation.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.util.Log
import android.view.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_sample.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        view_pager.adapter = MainAdapter(supportFragmentManager)
        bottom_bar.setupWithViewPager(view_pager)

        bottom_bar.setOnNavigationItemSelectedListener {
            Log.d("Clicks", it.toString())
            true
        }
    }
}

class MainAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int) = SampleFragment().apply {
        arguments = Bundle().apply {
            putInt("page", position + 1)
        }
    }

    override fun getCount() = 3

}

class SampleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sample, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        page.text = arguments!!.getInt("page").toString()
    }
}