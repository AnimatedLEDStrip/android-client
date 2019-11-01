package animatedledstrip.androidcontrol.animation

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
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import animatedledstrip.androidcontrol.R
import animatedledstrip.androidcontrol.utils.animationData
import animatedledstrip.androidcontrol.utils.indexOfChildOrNull
import animatedledstrip.androidcontrol.utils.presetColors
import animatedledstrip.colors.ColorContainer
import animatedledstrip.colors.ccpresets.*
import animatedledstrip.utils.toARGB
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.color.colorChooser
import kotlinx.android.synthetic.main.fragment_color_select.*
import top.defaults.drawabletoolbox.DrawableBuilder
import kotlin.math.roundToInt

class ColorSelect : Fragment() {

    /* Buttons */

    private var selectedColorButton: Button? = null
    private val presetButtons = mutableMapOf<Button, List<Long>>()

    private val colorButtons: LinearLayout by lazy {
        color_buttons_container
    }

    /* Button Sizes */

    private var colorButtonSize: Int = 0
    private var presetListHeight: Int = 0
    private var presetColorWidth: Int = 0
    private var presetColorHeight: Int = 0

    private val presetLayout: LinearLayout.LayoutParams by lazy {
        LinearLayout.LayoutParams(presetColorWidth, presetColorHeight).apply {
            gravity = Gravity.CENTER_HORIZONTAL
            bottomMargin = TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics)
                .roundToInt()
        }
    }


    /* Colors */

    private val colorContainer = ColorContainer()

    private val primaryColors =
        listOf(CCBlack, CCRed, CCGreen, CCBlue, CCYellow, CCCyan, CCMagenta)

    private val colors =
        (primaryColors + CCPresets.minus(primaryColors).minus(CCAqua))
            .map { it.color.toARGB() }
            .toIntArray()

    private val currentColor: Int
        get() = (colorContainer.colors
            .getOrNull(colorButtons.indexOfChildOrNull(selectedColorButton) ?: -1)
            ?: 0x0).toInt()

    private val presetButtonColor: Long by lazy {
        ResourcesCompat.getColor(resources, R.color.colorPrimary, null).toLong()
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

    private val clearColorsListener = View.OnClickListener {
        require(it is Button)
        resetColorButtons()
        colorContainer.colors.clear()
        it.visibility = Button.INVISIBLE
    }

    private val presetColorListener = View.OnClickListener {
        require(it is Button)
        removeColorButtons()
        addColorButtons(presetButtons[it] ?: throw IllegalStateException())
        addNewColorButton()
    }


    /* Drawable creation functions */

    private fun buttonDrawable(color: Long = CCBlack.color): Drawable =
        DrawableBuilder()
            .size(60)
            .oval()
            .solidColor(color.toARGB())
            .ripple()
            .build()

    private fun presetDrawable(colors: List<Long>): Drawable =
        GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            mutableListOf<Long>()
                .apply {
                    addAll(colors)
                    add(colors[0])
                }
                .map { it.toARGB() }
                .toIntArray()
        )


    /* Color button management functions */

    private fun addColorButton(color: Long = CCBlack.color) {
        clear_colors_button.visibility = Button.VISIBLE
        colorButtons.addView(
            Button(this.context).apply {
                background = buttonDrawable(color)
                colorContainer.colors += color
                text = ""
                layoutParams = LinearLayout.LayoutParams(colorButtonSize, colorButtonSize)
                setOnClickListener(colorListener)
            }
        )
    }

    private fun addColorButtons(colors: List<Long>) {
        for (c in colors) {
            addColorButton(c)
        }
    }

    private fun addNewColorButton() {
        colorButtons.addView(
            Button(this.context).apply {
                background = buttonDrawable()
                text = "+"
                setTextColor(0xffffffff.toInt())
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 24.0f)
                typeface = Typeface.DEFAULT_BOLD
                layoutParams = LinearLayout.LayoutParams(colorButtonSize, colorButtonSize)
                setOnClickListener(newColorListener)
            }
        )
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
        MaterialDialog(this.context!!, BottomSheet()).show {
            colorChooser(
                colors,
                allowCustomArgb = true,
                initialSelection = currentColor,
                waitForPositiveButton = false
            ) { _, color: Int ->
                selectedColorButton?.background = buttonDrawable(color.toLong())

                colorContainer.colors[colorButtons.indexOfChild(selectedColorButton)] =
                    color.toLong()
            }
            positiveButton(text = "Add")
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

        colorButtonSize = TypedValue
            .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60f, resources.displayMetrics)
            .roundToInt()

        presetListHeight = TypedValue
            .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 325f, resources.displayMetrics)
            .roundToInt()

        presetColorHeight = TypedValue
            .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40f, resources.displayMetrics)
            .roundToInt()

        presetColorWidth = TypedValue
            .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 375f, resources.displayMetrics)
            .roundToInt()

        animationData.colors += colorContainer

        return thisView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val presets = Button(this.context).apply {
            background = buttonDrawable(presetButtonColor)
            text = context.getString(R.string.pre_text)
            setTextColor(0xffffffff.toInt())
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 12.0f)
            typeface = Typeface.DEFAULT_BOLD
            layoutParams = LinearLayout.LayoutParams(colorButtonSize, colorButtonSize)
            setOnClickListener(presetListener)
        }

        preset_button_container.addView(presets)

        presetColors.forEach { presetColor ->
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
