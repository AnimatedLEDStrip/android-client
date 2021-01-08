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

package animatedledstrip.androidcontrol.views

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import animatedledstrip.androidcontrol.R
import animatedledstrip.colors.ColorContainer
import animatedledstrip.colors.isEmpty
import animatedledstrip.colors.toARGB
import kotlinx.android.synthetic.main.fragment_color_view.*

/**
 * Represents a ColorContainer for a running animation listing
 */
class ColorGradientViewer(private val cc: ColorContainer, private val includeTitle: Boolean) :
    Fragment() {

    constructor(
        colorList: List<Int>,
        includeTitle: Boolean
    ) : this(ColorContainer(colorList.toMutableList()), includeTitle)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_color_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        color_gradient.background = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            mutableListOf<Int>().apply {
                if (cc.isEmpty()) {
                    add(0)
                    add(0)
                } else {
                    addAll(cc.colors)
                    add(cc.colors[0])
                }
            }.map { it.toARGB() }.toIntArray()
        )
        if (!includeTitle) {
            color_view_title.text = ""

        }
    }
}
