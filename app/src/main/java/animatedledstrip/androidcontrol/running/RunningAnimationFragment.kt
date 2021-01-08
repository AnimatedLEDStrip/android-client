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

package animatedledstrip.androidcontrol.running

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import animatedledstrip.androidcontrol.R
import animatedledstrip.androidcontrol.utils.camelToCapitalizedWords
import animatedledstrip.androidcontrol.utils.mainSender
import animatedledstrip.androidcontrol.views.ColorGradientViewer
import animatedledstrip.client.send
import animatedledstrip.leds.animationmanagement.RunningAnimationParams
import animatedledstrip.leds.animationmanagement.endAnimation
import kotlinx.android.synthetic.main.fragment_animation.*

/**
 * Shows a single running animation along with its parameters and a button
 * for ending it
 */
class RunningAnimationFragment(private val params: RunningAnimationParams) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_animation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set parameters
        animation_id.text = params.id
        animation_name.text =
            getString(R.string.run_anim_label_animation, params.animationName)

        fun newParamTextView(paramName: String, paramValue: String): TextView =
            TextView(this.context!!).apply {
                text = getString(R.string.run_anim_label_param, paramName, paramValue)
                setTextColor(resources.getColor(R.color.colorText, null))
            }

        for (param in params.intParams)
            animation_intParams.addView(
                newParamTextView(
                    param.key.camelToCapitalizedWords(),
                    param.value.toString()
                )
            )
        for (param in params.doubleParams)
            animation_doubleParams.addView(
                newParamTextView(
                    param.key.camelToCapitalizedWords(),
                    param.value.toString()
                )
            )
        for (param in params.distanceParams)
            animation_distanceParams.addView(
                newParamTextView(
                    param.key.camelToCapitalizedWords(),
                    "%.2f, %.2f, %.2f".format(param.value.x, param.value.y, param.value.z)
                )
            )
        for (param in params.locationParams)
            animation_locationParams.addView(
                newParamTextView(
                    param.key.camelToCapitalizedWords(),
                    "%.2f, %.2f, %.2f".format(param.value.x, param.value.y, param.value.z)
                )
            )

        animation_direction.text =
            getString(R.string.run_anim_label_direction, params.direction.toString())

        // Set onClick listener for end button
        animation_end.setOnClickListener {
            check(it is Button)
            params.endAnimation().send(mainSender)
        }

        params.colors.forEach {
            childFragmentManager.beginTransaction()
                .add(animation_colors.id, ColorGradientViewer(it.toColorContainer()))
                .commit()
        }

//        val info = findAnimation(params.animationName).info

//        removeExcessData(animation_center, info.center)
//        removeExcessData(animation_delay, info.delay)
//        removeExcessData(animation_delay_mod, info.delay)
//        removeExcessData(animation_direction, info.direction)
//        removeExcessData(animation_distance, info.distance)
//        removeExcessData(animation_spacing, info.spacing)
//        if (!info.repetitive) animation_params.removeView(animation_continuous)
    }


    companion object {
        @JvmStatic
        fun newInstance(data: RunningAnimationParams) = RunningAnimationFragment(data)
    }
}
