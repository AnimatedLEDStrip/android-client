package animatedledstrip.androidcontrol.animation

import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import animatedledstrip.androidcontrol.R
import animatedledstrip.androidcontrol.utils.LockableScrollView
import animatedledstrip.androidcontrol.utils.animationData
import animatedledstrip.androidcontrol.utils.presetColors
import animatedledstrip.colors.ColorContainer
import animatedledstrip.colors.ccpresets.CCBlack
import animatedledstrip.utils.toARGB
import com.madrapps.pikolo.ColorPicker
import com.madrapps.pikolo.RGBColorPicker
import com.madrapps.pikolo.listeners.SimpleColorSelectionListener
import kotlinx.android.synthetic.main.fragment_color_select.*
import top.defaults.drawabletoolbox.DrawableBuilder
import kotlin.math.roundToInt

class ColorSelect : Fragment() {

    enum class State { NONE, PICKER, PRESETS }

    private var currentState = State.NONE
    lateinit var text: TextView
    lateinit var colorButtonsContainer: LinearLayout
    private lateinit var colorPickerContainer: ScrollView
    private lateinit var colorPicker: ColorPicker
    private lateinit var colorPickerList: LinearLayout
    private var selectedColor: Button? = null
    private val colorContainer = ColorContainer()
    private var buttonSize: Int = 0
    private var pickerSize: Int = 0
    private var colorWidth: Int = 0
    private var colorHeight: Int = 0


    private val presetListener = View.OnClickListener {
        changeState(if (currentState == State.PRESETS) State.NONE else State.PRESETS)
    }

    private val colorListener = View.OnClickListener {
        require(it is Button)

        if (selectedColor === it)
            when (currentState) {
                State.PICKER -> changeState(State.NONE)
                else -> changeState(State.PICKER)
            }
        else {
            changeState(State.PICKER)
            changeCurrentButton(it)
        }
    }

    private val presetButtons = mutableMapOf<Button, List<Long>>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val thisView = inflater.inflate(R.layout.fragment_color_select, container, false)

        buttonSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60f, resources.displayMetrics)
            .roundToInt()
        pickerSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 325f, resources.displayMetrics)
            .roundToInt()
        colorHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, resources.displayMetrics)
            .roundToInt()
        colorWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 375f, resources.displayMetrics)
            .roundToInt()
        colorPickerContainer =
            thisView.findViewById(R.id.color_picker_container)
        colorButtonsContainer =
            thisView.findViewById(R.id.color_buttons_container)
        colorPicker = RGBColorPicker(this.context!!)
        colorPicker.setColorSelectionListener(ColorListener())

        addNewColorButton()

        animationData.colors += colorContainer

        return thisView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val presets = Button(this.context).apply {
            background = DrawableBuilder()
                .size(60)
                .oval()
                .solidColor(ResourcesCompat.getColor(resources, R.color.colorPrimary, null))
                .ripple()
                .build()
            text = context.getString(R.string.pre_text)
            setTextColor(0xffffffff.toInt())
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 12.0f)
            typeface = Typeface.DEFAULT_BOLD
            layoutParams = LinearLayout.LayoutParams(buttonSize, buttonSize)
            setOnClickListener(presetListener)
        }

        preset_color_sets_button.addView(presets)
        colorPickerList = color_picker_list

        presetColors.forEach { presetColor ->
            val presetButton = Button(this.context!!).apply {
                background = GradientDrawable(
                    GradientDrawable.Orientation.LEFT_RIGHT,
                    mutableListOf<Long>().apply {
                        addAll(presetColor)
                        add(presetColor[0])
                    }.map { it.toARGB() }.toIntArray()
                )
                layoutParams = LinearLayout.LayoutParams(colorWidth, colorHeight).apply {
                    gravity = Gravity.CENTER_HORIZONTAL
                }
                setOnClickListener {
                    colorButtonsContainer.removeAllViews()
                    presetButtons[this]?.forEach {
                        addColorButton(it)
                    }
                    addNewColorButton()
                    changeState(State.NONE)
                }
            }
            presetButtons[presetButton] = presetColor
        }

        color_clear_button.setOnClickListener {
            require(it is Button)
            it.setOnClickListener { b ->
                resetColorButtons()
                b.visibility = Button.INVISIBLE
            }
        }

    }

    private fun addColorButton(color: Long = CCBlack.color) {
        colorButtonsContainer.addView(
            Button(this.context).apply {
                background = DrawableBuilder()
                    .size(60)
                    .oval()
                    .solidColor(color.toARGB())
                    .ripple()
                    .build()
                colorContainer.colors += color
                text = ""
                layoutParams = LinearLayout.LayoutParams(buttonSize, buttonSize)
                setOnClickListener(colorListener)
            }
        )
    }

    private fun addNewColorButton() {
        colorButtonsContainer.addView(
            Button(this.context).apply {
                background = DrawableBuilder()
                    .size(60)
                    .oval()
                    .solidColor(CCBlack.color.toARGB())
                    .ripple()
                    .build()
                text = "+"
                setTextColor(0xffffffff.toInt())
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 24.0f)
                typeface = Typeface.DEFAULT_BOLD
                layoutParams = LinearLayout.LayoutParams(buttonSize, buttonSize)
                setOnClickListener {
                    require(it is Button)
                    colorContainer += 0x0
                    color_clear_button.visibility = Button.VISIBLE
                    changeState(State.PICKER)
                    changeCurrentButton(it)
                    it.setOnClickListener(colorListener)
                    it.text = ""
                    addNewColorButton()
                }
            }
        )
    }

    private fun resetColorButtons() {
        colorButtonsContainer.removeAllViews()
        addNewColorButton()
        changeState(State.NONE)
    }

    private fun changeState(newState: State) {
        if (newState == currentState) return
        if (currentState != State.NONE) color_picker_list.removeAllViews()

        when (newState) {
            State.NONE -> colorPickerContainer.layoutParams.height = 0
            State.PICKER -> {
                colorPickerContainer.layoutParams.height = pickerSize
                colorPickerList.addView(colorPicker)
            }
            State.PRESETS -> {
                colorPickerContainer.layoutParams.height = pickerSize
                presetButtons.forEach { (b, _) ->
                    color_picker_list.addView(b)
                }
            }
        }
        currentState = newState
    }

    private fun changeCurrentButton(button: Button) {
        selectedColor = button
        colorPicker.setColor(colorContainer.colors[colorButtonsContainer.indexOfChild(button)].toInt())
    }

    companion object {
        @JvmStatic
        fun newInstance() = ColorSelect()
    }

    inner class ColorListener : SimpleColorSelectionListener() {
        override fun onColorSelected(color: Int) {
            if (selectedColor != null) {
                selectedColor!!.background =
                    DrawableBuilder()
                        .size(60)
                        .oval()
                        .solidColor(color.toARGB())
                        .ripple()
                        .build()
                colorContainer.colors[colorButtonsContainer.indexOfChild(selectedColor)] =
                    color + 0x1000000L
            }
        }

        override fun onColorSelectionStart(color: Int) {
            (colorPicker.parent.parent.parent.parent.parent.parent as LockableScrollView).scrollingEnabled =
                false
            super.onColorSelectionStart(color)
        }

        override fun onColorSelectionEnd(color: Int) {
            (colorPicker.parent.parent.parent.parent.parent.parent as LockableScrollView).scrollingEnabled =
                true
            super.onColorSelectionEnd(color)
        }
    }
}
