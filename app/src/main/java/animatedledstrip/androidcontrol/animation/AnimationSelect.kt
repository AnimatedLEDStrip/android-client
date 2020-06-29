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

package animatedledstrip.androidcontrol.animation

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import animatedledstrip.androidcontrol.R
import animatedledstrip.androidcontrol.utils.animationData
import animatedledstrip.androidcontrol.utils.animationOptionAdapter
import animatedledstrip.androidcontrol.utils.mainSender
import animatedledstrip.animationutils.Animation
import animatedledstrip.animationutils.AnimationData
import animatedledstrip.animationutils.ParamUsage
import kotlinx.android.synthetic.main.fragment_animation_select.*

/**
 * Select the animation type.
 *
 * Adds the corresponding fragments for configuring that animation.
 */
class AnimationSelect : Fragment(), AdapterView.OnItemSelectedListener {

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_animation_select, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animation_list.adapter = animationOptionAdapter
        animation_list.onItemSelectedListener = this
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        check(context is OnFragmentInteractionListener)
        listener = context
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent === animation_list) resetView()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    fun resetView() {
        val item = animation_list.selectedItem.toString()
        Log.d("RView", "Selected $item")
        animation_options.removeAllViews()
        animationData = AnimationData()
        addAnimationOptions(mainSender.supportedAnimations[item] ?: return)
    }

    private fun addAnimationOptions(info: Animation.AnimationInfo) {
        Log.d("AnimOpts", "Adding opts for ${info.name}")
        animationData.animation = info.name

        fun addOptionFrag(frag: Fragment) {
            childFragmentManager
                .beginTransaction()
                .add(animation_options.id, frag)
                .commit()
        }

        if (info.repetitive) addOptionFrag(ContinuousSelect.newInstance())
        for (i in 0 until info.numColors) addOptionFrag(ColorSelect.newInstance())
        if (info.center != ParamUsage.NOTUSED) addOptionFrag(CenterSelect.newInstance())
        if (info.distance != ParamUsage.NOTUSED) addOptionFrag(DistanceSelect.newInstance())
        if (info.direction != ParamUsage.NOTUSED) addOptionFrag(DirectionSelect.newInstance())
        if (info.spacing != ParamUsage.NOTUSED) addOptionFrag(SpacingSelect.newInstance())
    }


    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance() = AnimationSelect()
    }
}
