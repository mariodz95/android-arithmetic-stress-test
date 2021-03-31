package com.example.arithmeticstresstest.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.arithmeticstresstest.fragment.GlucoseDataFragment
import com.example.arithmeticstresstest.fragment.MyDataFragment
import com.example.arithmeticstresstest.fragment.SmartDeviceFragment
import com.example.arithmeticstresstest.fragment.TestDataFragment

class MyAdapter(
    private val myContext: MyDataFragment,
    fm: FragmentManager,
    internal var totalTabs: Int
) : FragmentPagerAdapter(fm) {

    // tab titles
    private val tabTitles = arrayOf("Test data", "Glucose levels", "Smart device data")

    // overriding getPageTitle()
    override fun getPageTitle(position: Int): CharSequence? {
        return tabTitles[position]
    }

    // this is for fragment tabs
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                return TestDataFragment()
            }
            1 -> {
                return GlucoseDataFragment()
            }
            2 -> {
                return SmartDeviceFragment()
            }
            else -> return TestDataFragment()
        }
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }
}