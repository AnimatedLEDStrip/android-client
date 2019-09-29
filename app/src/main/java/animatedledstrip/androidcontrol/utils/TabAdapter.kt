package animatedledstrip.androidcontrol.utils

import android.content.Context
import androidx.fragment.app.FragmentPagerAdapter
import animatedledstrip.androidcontrol.R
import animatedledstrip.androidcontrol.animation.AnimationSelect
import animatedledstrip.androidcontrol.connections.ConnectFragment

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class TabAdapter(private val context: Context, fm: androidx.fragment.app.FragmentManager) : androidx.fragment.app.FragmentPagerAdapter(fm) {

    private val tabTitles = arrayOf(
        R.string.tab_text_1,
        R.string.tab_text_2
    )

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        // getItem is called to instantiate the fragment for the given page.
        return when (position) {
            0 -> ConnectFragment.newInstance()
            1 -> AnimationSelect.newInstance()
            else -> throw Exception()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(tabTitles[position])
    }

    override fun getCount() = 2
}