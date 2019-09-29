package animatedledstrip.androidcontrol.animation

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import animatedledstrip.androidcontrol.R
import animatedledstrip.androidcontrol.utils.LockableScrollView
import animatedledstrip.androidcontrol.utils.animationData
import animatedledstrip.colors.ColorContainer
import animatedledstrip.colors.ccpresets.CCBlack
import animatedledstrip.utils.toARGB
import com.madrapps.pikolo.ColorPicker
import com.madrapps.pikolo.RGBColorPicker
import com.madrapps.pikolo.listeners.SimpleColorSelectionListener
import top.defaults.drawabletoolbox.DrawableBuilder

class ColorSelect : Fragment() {

    lateinit var text: TextView

    lateinit var colorButtonsContainer: LinearLayout

    private lateinit var colorPickerContainer: ConstraintLayout
    private lateinit var colorPicker: ColorPicker

    private var pickerVisible = false
    private var selectedColor: Button? = null

    private val colorContainer = ColorContainer()

    inner class ColorListener : SimpleColorSelectionListener() {
        override fun onColorSelected(color: Int) {
            if (selectedColor != null) {
                selectedColor!!.background =
                    DrawableBuilder()
                        .size(60)
                        .oval()
                        .solidColor(color or 0xFF000000.toInt())
                        .ripple()
                        .build()
                colorContainer.colors[colorButtonsContainer.indexOfChild(selectedColor)] =
                    color + 0x1000000L
            }
        }

        override fun onColorSelectionStart(color: Int) {
            (colorPicker.parent.parent.parent.parent.parent as LockableScrollView).scrollingEnabled = false
            super.onColorSelectionStart(color)
        }

        override fun onColorSelectionEnd(color: Int) {
            (colorPicker.parent.parent.parent.parent.parent as LockableScrollView).scrollingEnabled = true
            Log.d("Color", animationData.colors.toString())
            super.onColorSelectionEnd(color)
        }
    }


    private val colorListener = View.OnClickListener {
        if (pickerVisible && selectedColor === it) {
            colorPickerContainer.removeView(colorPicker)
            colorPickerContainer.layoutParams.height = 0
            pickerVisible = false
        } else {
            if (!pickerVisible) colorPickerContainer.addView(colorPicker)
            colorPickerContainer.layoutParams.height = 850
            pickerVisible = true
            selectedColor = it as Button
            colorPicker.setColor(
                colorContainer.colors[colorButtonsContainer.indexOfChild(it)].toInt()
            )
        }
    }

    private val newColorListener = View.OnClickListener {
        if (!pickerVisible) colorPickerContainer.addView(colorPicker)
        colorPickerContainer.layoutParams.height = 850
        pickerVisible = true
        colorPicker.setColor(0)
        it.setOnClickListener(colorListener)
        (it as Button).text = ""
        selectedColor = it
        colorButtonsContainer.addView(newColorButton())
        colorContainer.colors += 0x0
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val thisView = inflater.inflate(R.layout.fragment_color_select, container, false)

        colorPickerContainer =
            thisView.findViewById(R.id.color_picker_container)
        colorButtonsContainer =
            thisView.findViewById(R.id.color_buttons_container)
        colorPicker = RGBColorPicker(this.context!!)
        colorPicker.setColorSelectionListener(ColorListener())

        colorButtonsContainer.addView(newColorButton())

        animationData.colors += colorContainer

        return thisView
    }

    private fun newColorButton(): Button = Button(this.context).apply {
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
        layoutParams = LinearLayout.LayoutParams(150, 150)
        setOnClickListener(newColorListener)
    }

    companion object {
        @JvmStatic
        fun newInstance() = ColorSelect()
    }
}
