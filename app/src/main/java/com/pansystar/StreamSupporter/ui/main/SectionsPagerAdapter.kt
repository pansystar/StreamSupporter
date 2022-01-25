package com.pansystar.StreamSupporter.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.pansystar.StreamSupporter.FragSetting
import com.pansystar.StreamSupporter.FragSoundEffect
import com.pansystar.StreamSupporter.MainActivity
import com.pansystar.StreamSupporter.R

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2,
    R.string.tab_text_3,
    R.string.tab_text_4,
    R.string.tab_text_5,
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: MainActivity, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {



    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).

        var mainActivity: MainActivity = context as MainActivity

        when (position) {
            0 -> {
                var instance = FragVoiceRecognize()
                instance.init(mainActivity)
                return instance
            }
            1 -> {
                var instance = FragSoundEffect()
                instance.init(mainActivity)
                return instance
            }
            3 -> {
                return FragSetting()
            }
            else -> {
                return PlaceholderFragment.newInstance(position + 1)
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return TAB_TITLES.count()
    }
}