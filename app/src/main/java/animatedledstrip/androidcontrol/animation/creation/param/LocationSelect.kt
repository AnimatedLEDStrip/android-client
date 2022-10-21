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

package animatedledstrip.androidcontrol.animation.creation.param

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import animatedledstrip.androidcontrol.R
import animatedledstrip.androidcontrol.animation.creation.popup.LocationEditPopup
import animatedledstrip.androidcontrol.utils.camelToCapitalizedWords
import animatedledstrip.animations.AnimationParameter
import animatedledstrip.leds.locationmanagement.Location
import kotlinx.android.synthetic.main.fragment_location_select.*

/**
 * Set a location property of the animation.
 */
class LocationSelect(val parameter: AnimationParameter<Location>) : Fragment() {

    private fun showEditDialog() {
        val dialog = LocationEditPopup(
            location_param_value_text.text
                .removePrefix("${parameter.name.camelToCapitalizedWords()}: ")
                .removeSuffix("null")
                .split(",")
                .let {
                    Location(
                        it.getOrNull(0)?.toDoubleOrNull() ?: 0.0,
                        it.getOrNull(1)?.toDoubleOrNull() ?: 0.0,
                        it.getOrNull(2)?.toDoubleOrNull() ?: 0.0,
                    )
                },
            parameter.name,
            frag = this
        )
        dialog.show(parentFragmentManager, "${parameter}EditPopup")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_location_select, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        location_param_value_text.text = getString(
            R.string.run_anim_label_param,
            parameter.name.camelToCapitalizedWords(),
            parameter.default?.coordinates
        )
        location_param_card.setOnClickListener {
            showEditDialog()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(parameter: AnimationParameter<Location>) = LocationSelect(parameter)
    }

}
