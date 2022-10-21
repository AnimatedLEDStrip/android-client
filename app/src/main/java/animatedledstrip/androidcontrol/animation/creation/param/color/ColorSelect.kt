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

package animatedledstrip.androidcontrol.animation.creation.param.color

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import animatedledstrip.androidcontrol.R
import animatedledstrip.androidcontrol.utils.animParams
import animatedledstrip.androidcontrol.utils.indexOfChildOrNull
import animatedledstrip.colors.ColorContainer
import animatedledstrip.colors.ccpresets.*
import animatedledstrip.colors.toARGB
import animatedledstrip.leds.animationmanagement.addColor
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.color.colorChooser
import kotlinx.android.synthetic.main.fragment_color_select.*
import top.defaults.drawabletoolbox.DrawableBuilder

/**
 * Select the colors for the animation.
 *
 * One fragment is created for each required and optional color as specified
 * in the AnimationInfo for that animation.
 */
class ColorSelect : Fragment() {

    /* Buttons */

    private var selectedColorButton: Button? = null
    private val presetButtons = mutableMapOf<Button, ColorContainer>()

    private val colorButtons: LinearLayout by lazy {
        color_buttons_container
    }


    /* Button Sizes */

    private val colorButtonSize: Int by lazy {
        resources.getDimensionPixelSize(R.dimen.color_button_size)
    }
    private val presetListHeight: Int by lazy {
        resources.getDimensionPixelSize(R.dimen.preset_list_height)
    }
    private val presetColorWidth: Int by lazy {
        resources.getDimensionPixelSize(R.dimen.preset_color_width)
    }
    private val presetColorHeight: Int by lazy {
        resources.getDimensionPixelSize(R.dimen.preset_color_height)
    }

    private val presetLayout: LinearLayout.LayoutParams by lazy {
        LinearLayout.LayoutParams(presetColorWidth, presetColorHeight).apply {
            gravity = Gravity.CENTER_HORIZONTAL
            bottomMargin = resources.getDimensionPixelSize(R.dimen.preset_color_margin)
        }
    }


    /* Colors */

    private val colorContainer = ColorContainer()

    private val primaryColors =
        listOf(ColorContainer.Black, ColorContainer.Red, ColorContainer.Green, ColorContainer.Blue, ColorContainer.Yellow, ColorContainer.Cyan, ColorContainer.Magenta)

    private val colors =
        (primaryColors + CCPresets.minus(primaryColors.toSet()).minus(ColorContainer.Aqua))
            .map { it.color.toARGB() }
            .toIntArray()

    private val currentColor: Int
        get() = (colorContainer.colors
            .getOrNull(colorButtons.indexOfChildOrNull(selectedColorButton) ?: -1)
            ?: 0x0).toInt()

    private val presetButtonColor: Int by lazy {
        resources.getColor(R.color.colorPrimary, null)
    }


    /* Listeners */

    private val presetListener = View.OnClickListener {
        togglePresets()
    }

    private val colorListener = View.OnClickListener {
        require(it is Button)
        changeCurrentButton(it)
        chooseColor()
    }

    private val newColorListener = View.OnClickListener {
        require(it is Button)
        colorContainer += 0x0
        clear_colors_button.visibility = Button.VISIBLE
        changeCurrentButton(it)
        it.setOnClickListener(colorListener)
        it.text = ""
        chooseColor()
        addNewColorButton()
    }

    private val removeColorListener = View.OnLongClickListener {
        require(it is Button)
        colorButtons.removeView(it)
        true
    }

    private val clearColorsListener = View.OnClickListener {
        require(it is Button)
        resetColorButtons()
        colorContainer.colors.clear()
        it.visibility = Button.INVISIBLE
    }

    private val presetColorListener = View.OnClickListener {
        require(it is Button)
        removeColorButtons()
        colorContainer.colors.clear()
        addColorButtons(presetButtons[it] ?: throw IllegalStateException())
        addNewColorButton()
    }


    /* Drawable creation functions */

    private fun buttonDrawable(color: Int = ColorContainer.Black.color): Drawable =
        DrawableBuilder()
            .size(60)
            .oval()
            .solidColor(color.toARGB())
            .ripple()
            .build()

    private fun presetDrawable(cc: ColorContainer): Drawable =
        GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            mutableListOf<Int>()
                .apply {
                    addAll(cc.colors)
                    add(cc.colors[0])
                }
                .map { it.toARGB() }
                .toIntArray()
        )


    /* Color button management functions */

    private fun colorButton(
        context: Context?,
        newButton: Boolean,
        color: Int = ColorContainer.Black.color
    ): Button =
        Button(context).apply {
            background = buttonDrawable(color)
            text = if (newButton) "+" else ""
            setTextColor(0xffffffff.toInt())
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 24.0f)
            typeface = Typeface.DEFAULT_BOLD
            layoutParams = LinearLayout.LayoutParams(colorButtonSize, colorButtonSize)
            setOnClickListener(if (newButton) newColorListener else colorListener)
            setOnLongClickListener(if (newButton) null else removeColorListener)
        }

    private fun addColorButton(color: Int = ColorContainer.Black.color) {
        clear_colors_button.visibility = Button.VISIBLE
        colorButtons.addView(colorButton(this.context, newButton = false, color = color))
        colorContainer += color
    }

    private fun addColorButtons(cc: ColorContainer) {
        for (c in cc.colors) {
            addColorButton(c)
        }
    }

    private fun addNewColorButton() {
        colorButtons.addView(colorButton(this.context, newButton = true))
    }

    private fun removeColorButtons() {
        colorButtons.removeAllViews()
    }

    private fun resetColorButtons() {
        removeColorButtons()
        addNewColorButton()
    }

    private fun changeCurrentButton(button: Button) {
        selectedColorButton = button
    }


    /* Color choice functions */

    private fun chooseColor() {
        MaterialDialog(this.context ?: return, BottomSheet()).show {
            colorChooser(
                colors,
                allowCustomArgb = true,
                initialSelection = currentColor,
                waitForPositiveButton = false
            ) { _, color: Int ->
                selectedColorButton?.background = buttonDrawable(color)

                colorContainer.colors[colorButtons.indexOfChild(selectedColorButton)] =
                    color
            }
            positiveButton(text = getString(R.string.anim_color_button_add))
        }
    }

    private fun togglePresets() {
        presets_container.layoutParams.height =
            if (presets_container.layoutParams.height != 0) 0
            else presetListHeight
        presets_container.layoutParams = presets_container.layoutParams     // So changes are shown
    }


    /* OnCreation overrides */

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val thisView = inflater.inflate(R.layout.fragment_color_select, container, false)

        animParams.addColor(colorContainer)

        return thisView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val presets = Button(this.context).apply {
            background = buttonDrawable(presetButtonColor)
            text = context.getString(R.string.anim_color_button_presets)
            setTextColor(0xffffffff.toInt())
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 12.0f)
            typeface = Typeface.DEFAULT_BOLD
            layoutParams = LinearLayout.LayoutParams(colorButtonSize, colorButtonSize)
            setOnClickListener(presetListener)

        }

        preset_button_container.addView(presets)

        CCGroupPresets.forEach { presetColor ->
            Button(this.context ?: throw IllegalStateException()).apply {
                background = presetDrawable(presetColor)
                layoutParams = presetLayout
                setOnClickListener(presetColorListener)
                presetButtons[this] = presetColor
            }
        }

        clear_colors_button.setOnClickListener(clearColorsListener)

        presetButtons.forEach { (b, _) ->
            presets_list.addView(b)
        }

        addNewColorButton()
    }

    companion object {
        @JvmStatic
        fun newInstance() = ColorSelect()
    }
}
