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

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import animatedledstrip.androidcontrol.R
import animatedledstrip.animationutils.Animation
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.fragment_color_select_container.*

/**
 * Holds all the fragments for creating an animation to send to the server.
 */
class ColorSelectContainerFragment(private val minimumColors: Int, private val unlimitedColors: Boolean) :
    Fragment() {

    private val newColorListener = View.OnClickListener {
        check(it is Chip)
        Log.d("Chip", "Pressed")
        newColor()
    }

    private lateinit var addColorFragment: AddAnotherColorFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_color_select_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val transaction = childFragmentManager.beginTransaction()

        for (i in 0 until minimumColors)
            transaction.add(
                color_select_container.id,
                ColorSelect.newInstance()
            )

        if (unlimitedColors) {
            addColorFragment = AddAnotherColorFragment(newColorListener)
            transaction.add(
                color_select_container.id,
                addColorFragment
            )
        }

        transaction.commit()
    }

    private fun newColor() {
        childFragmentManager
            .beginTransaction()
            .remove(addColorFragment)
            .add(color_select_container.id, ColorSelect.newInstance())
            .commit()

        addColorFragment = AddAnotherColorFragment(newColorListener)

        childFragmentManager
            .beginTransaction()
            .add(color_select_container.id, addColorFragment)
            .commit()
    }

    companion object {
        @JvmStatic
        fun newInstance(info: Animation.AnimationInfo) =
            ColorSelectContainerFragment(info.minimumColors, info.unlimitedColors)
    }
}
