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
import animatedledstrip.androidcontrol.utils.alsClient
import animatedledstrip.androidcontrol.utils.camelToCapitalizedWords
import animatedledstrip.androidcontrol.views.ColorGradientViewer
import animatedledstrip.leds.animationmanagement.RunningAnimationParams
import kotlinx.android.synthetic.main.fragment_running_animation.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Shows a single running animation along with its parameters and a button
 * for ending it
 */
class RunningAnimationFragment(private val params: RunningAnimationParams) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_running_animation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set parameters
        animation_id.text = params.id
        animation_name.text =
            getString(R.string.run_anim_label_animation, params.animationName)
        animation_run_count.text =
            getString(
                R.string.run_anim_label_run_count,
                if (params.runCount == -1) "Endless" else params.runCount.toString()
            )

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
        for (param in params.locationParams)
            animation_locationParams.addView(
                newParamTextView(
                    param.key.camelToCapitalizedWords(),
                    "%.2f, %.2f, %.2f".format(param.value.x, param.value.y, param.value.z)
                )
            )
        for (param in params.distanceParams)
            animation_distanceParams.addView(
                newParamTextView(
                    param.key.camelToCapitalizedWords(),
                    "%.2f, %.2f, %.2f".format(param.value.x, param.value.y, param.value.z)
                )
            )
        for (param in params.rotationParams)
            animation_locationParams.addView(
                newParamTextView(
                    param.key.camelToCapitalizedWords(),
                    "%.2f, %.2f, %.2f (%s)".format(param.value.xRotation, param.value.yRotation, param.value.zRotation, param.value.rotationOrder.joinToString { "${it.name[0]}," })
                )
            )

        // Set onClick listener for end button
        animation_end.setOnClickListener {
            check(it is Button)
            GlobalScope.launch(Dispatchers.IO) {
                alsClient?.endAnimation(params)
            }
        }

        params.colors.forEach {
            childFragmentManager.beginTransaction()
                .add(animation_colors.id, ColorGradientViewer(it.toColorContainer(), true))
                .commit()
        }
    }
}
