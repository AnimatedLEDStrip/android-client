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
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import animatedledstrip.androidcontrol.R
import animatedledstrip.androidcontrol.animation.creation.AnimationCreationContainer
import animatedledstrip.androidcontrol.animation.select.AnimationSelectionContainer
import animatedledstrip.androidcontrol.connections.ServerListContainer
import animatedledstrip.androidcontrol.running.RunningAnimationsContainer
import animatedledstrip.androidcontrol.stripinfo.StripInfoContainer
import animatedledstrip.androidcontrol.utils.alsClient
import animatedledstrip.androidcontrol.utils.animationPropertyOptionAdapter

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class TabAdapter(private val context: Context, fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val tabs = listOf(
        R.string.tab_1_server to ServerListContainer::class,
        R.string.tab_2_running to RunningAnimationsContainer::class,
        R.string.tab_3_run to AnimationSelectionContainer::class,
        R.string.tab_4_create to AnimationCreationContainer::class,
        R.string.tab_5_strip_info to StripInfoContainer::class,
    )

    override fun getItem(position: Int): Fragment {
        var tabToUse = tabs[position].second
        if (alsClient == null && tabToUse != ServerListContainer::class)
            tabToUse = SelectFirstPlaceholder::class
        return when (tabToUse) {
            ServerListContainer::class -> ServerListContainer()
            AnimationCreationContainer::class -> {
                if (animationPropertyOptionAdapter.isEmpty)
                    ContactingServerPlaceholder()
                else AnimationCreationContainer()
            }
            AnimationSelectionContainer::class -> AnimationSelectionContainer()
            RunningAnimationsContainer::class -> RunningAnimationsContainer()
            StripInfoContainer::class -> StripInfoContainer()
            SelectFirstPlaceholder::class -> SelectFirstPlaceholder()
            else -> error("Trying to load an invalid tab")
        }
    }

    override fun getItemPosition(obj: Any): Int =
        when (obj) {
            is ServerListContainer -> PagerAdapter.POSITION_UNCHANGED
            is SelectFirstPlaceholder ->
                if (alsClient != null) PagerAdapter.POSITION_NONE
                else PagerAdapter.POSITION_UNCHANGED
            is AnimationCreationContainer, is AnimationSelectionContainer, is RunningAnimationsContainer ->
                if (alsClient != null) PagerAdapter.POSITION_UNCHANGED
                else PagerAdapter.POSITION_NONE
            else -> PagerAdapter.POSITION_NONE
        }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(tabs[position].first)
    }

    override fun getCount() = tabs.size
}
