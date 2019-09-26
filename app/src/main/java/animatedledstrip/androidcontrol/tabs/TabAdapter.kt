package animatedledstrip.androidcontrol.tabs

import android.content.Context
import androidx.fragment.app.FragmentPagerAdapter
import animatedledstrip.androidcontrol.R
import animatedledstrip.androidcontrol.animation.AnimationSelect

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
        // Return a PlaceholderFragment (defined as a static inner class below).
        return when (position) {
            0 -> AnimationSelect.newInstance()
            1 -> PlaceholderFragment.newInstance(2)
            else -> throw Exception()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(tabTitles[position])
    }

    override fun getCount() = 2
}