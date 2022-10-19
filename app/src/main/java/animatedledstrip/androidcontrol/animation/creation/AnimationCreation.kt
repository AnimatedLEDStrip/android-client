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

package animatedledstrip.androidcontrol.animation.creation

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
import animatedledstrip.androidcontrol.utils.alsClient
import animatedledstrip.androidcontrol.utils.animParams
import animatedledstrip.androidcontrol.utils.animationOptionAdapter
import animatedledstrip.animations.Animation
import animatedledstrip.animations.AnimationParameter
import animatedledstrip.leds.animationmanagement.AnimationToRunParams
import kotlinx.android.synthetic.main.fragment_animation_select.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

/**
 * Select the animation type.
 *
 * Adds the corresponding fragments for configuring that animation.
 */
class AnimationCreation : Fragment(), AdapterView.OnItemSelectedListener {

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
        animation_options.removeAllViews()
        animParams = AnimationToRunParams()
        var info: Animation.AnimationInfo?
        runBlocking(Dispatchers.IO) {
            info = alsClient?.getAnimationInfo(item)
        }
        addAnimationOptions(info ?: return)
    }

    private fun addAnimationOptions(info: Animation.AnimationInfo) {
        Log.d("AnimOpts", "Adding opts for ${info.name}")
        animParams.animation = info.name

        fun addOptionFrag(frag: Fragment) {
            childFragmentManager
                .beginTransaction()
                .add(animation_options.id, frag)
                .commit()
        }

        addOptionFrag(IntSelect(AnimationParameter("Run Count", "", info.runCountDefault)))
        addOptionFrag(ColorSelectContainerFragment(info.minimumColors, info.unlimitedColors))
        for (param in info.intParams) addOptionFrag(IntSelect(param))
        for (param in info.doubleParams) addOptionFrag(DoubleSelect(param))
//        for (param in info.stringParams)
        for (param in info.locationParams) addOptionFrag(LocationSelect(param))
        for (param in info.distanceParams) addOptionFrag(DistanceSelect(param))
        for (param in info.rotationParams) addOptionFrag(RotationSelect(param))
//        for (param in info.equationParams)
    }


    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }
}
