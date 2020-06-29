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
import androidx.fragment.app.Fragment
import animatedledstrip.androidcontrol.R
import animatedledstrip.androidcontrol.utils.AnimationColor
import animatedledstrip.animationutils.AnimationData
import animatedledstrip.animationutils.ParamUsage
import animatedledstrip.animationutils.findAnimation
import animatedledstrip.client.endAnimation
import kotlinx.android.synthetic.main.fragment_animation.*

/**
 * Shows a single running animation along with its parameters and a button
 * for ending it.
 */
class AnimationFragment(private val data: AnimationData) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_animation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animation_id.text = data.id
        animation_name.text = getString(R.string.run_anim_label_animation, data.animation.toString())
        animation_center.text = getString(R.string.run_anim_label_center, data.center.toString())
        animation_continuous.text = getString(
            R.string.run_anim_label_continuous,
            (data.continuous ?: findAnimation(data.animation)?.info?.repetitive).toString()
        )
        animation_delay.text = getString(R.string.run_anim_label_delay, data.delay.toString())
        animation_delay_mod.text = getString(R.string.run_anim_label_delay_mod, data.delayMod.toString())
        animation_direction.text = getString(R.string.run_anim_label_direction, data.direction.toString())
        animation_distance.text = getString(R.string.run_anim_label_distance, data.distance.toString())
        animation_spacing.text = getString(R.string.run_anim_label_spacing, data.spacing.toString())

        animation_end.setOnClickListener {
            check(it is Button)
            data.endAnimation()
            it.text = getString(R.string.run_anim_end_anim_button_ending)
        }

        fun removeExcessData(view: View, reqLevel: ParamUsage) {
            if (reqLevel == ParamUsage.NOTUSED) animation_params.removeView(view)
        }

        data.colors.forEach {
            childFragmentManager.beginTransaction()
                .add(
                    animation_colors.id,
                    AnimationColor(it.toColorContainer().colors)
                )
                .commit()
        }

        val info = findAnimation(data.animation)?.info ?: return

        removeExcessData(animation_center, info.center)
        removeExcessData(animation_delay, info.delay)
        removeExcessData(animation_delay_mod, info.delay)
        removeExcessData(animation_direction, info.direction)
        removeExcessData(animation_distance, info.distance)
        removeExcessData(animation_spacing, info.spacing)
        if (!info.repetitive) animation_params.removeView(animation_continuous)
    }


    companion object {
        @JvmStatic
        fun newInstance(data: AnimationData) = AnimationFragment(data)
    }
}
