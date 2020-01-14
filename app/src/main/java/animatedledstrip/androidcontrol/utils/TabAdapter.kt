/*
 *  Copyright (c) 2020 AnimatedLEDStrip
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package animatedledstrip.androidcontrol.utils

import android.content.Context
import androidx.fragment.app.FragmentPagerAdapter
import animatedledstrip.androidcontrol.R
import animatedledstrip.androidcontrol.animation.AnimationSelectContainer
import animatedledstrip.androidcontrol.connections.ConnectFragment
import animatedledstrip.androidcontrol.running.RunningAnimations

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class TabAdapter(private val context: Context, fm: androidx.fragment.app.FragmentManager) : androidx.fragment.app.FragmentPagerAdapter(fm) {

    private val tabTitles = arrayOf(
        R.string.tab_text_1_server,
        R.string.tab_text_2_send,
        R.string.tab_text_3_running
    )

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        // getItem is called to instantiate the fragment for the given page.
        return when (position) {
            0 -> ConnectFragment.newInstance()
            1 -> AnimationSelectContainer.newInstance()
            2 -> RunningAnimations.newInstance()
            else -> throw Exception()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(tabTitles[position])
    }

    override fun getCount() = 3
}