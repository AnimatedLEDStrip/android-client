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

package animatedledstrip.androidcontrol.tabs

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import animatedledstrip.androidcontrol.R
import animatedledstrip.androidcontrol.animation.AnimationSelectContainer
import animatedledstrip.androidcontrol.connections.ConnectFragment
import animatedledstrip.androidcontrol.running.RunningAnimationsContainer
import animatedledstrip.androidcontrol.utils.animationOptionAdapter
import animatedledstrip.androidcontrol.utils.mainSender
import kotlin.reflect.KClass

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class TabAdapter(private val context: Context, fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val tabs = listOf<Pair<Int, KClass<out Fragment>>>(
        R.string.tab_1_server to ConnectFragment::class,
        R.string.tab_2_send to AnimationSelectContainer::class,
        R.string.tab_3_running to RunningAnimationsContainer::class
    )

    override fun getItem(position: Int): Fragment {
        var tabToUse = tabs[position].second
        if (!mainSender.connected && tabToUse != ConnectFragment::class)
            tabToUse = ConnectFirstPlaceholder::class
        Log.d("Tab", tabToUse.toString())
        return when (tabToUse) {
            ConnectFragment::class -> ConnectFragment.newInstance()
            AnimationSelectContainer::class -> {
                Log.d("Click", animationOptionAdapter.toString())
                AnimationSelectContainer.newInstance()
            }
            RunningAnimationsContainer::class -> RunningAnimationsContainer.newInstance()
            ConnectFirstPlaceholder::class -> ConnectFirstPlaceholder.newInstance()
            else -> throw Exception()
        }
    }

    override fun getItemPosition(obj: Any): Int =
        when (obj) {
            is ConnectFragment -> PagerAdapter.POSITION_UNCHANGED
            is ConnectFirstPlaceholder ->
                if (mainSender.connected) PagerAdapter.POSITION_NONE
                else PagerAdapter.POSITION_UNCHANGED
            is AnimationSelectContainer, is RunningAnimationsContainer ->
                if (mainSender.connected) PagerAdapter.POSITION_UNCHANGED
                else PagerAdapter.POSITION_NONE
            else -> PagerAdapter.POSITION_NONE
        }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(tabs[position].first)
    }

    override fun getCount() = tabs.size
}