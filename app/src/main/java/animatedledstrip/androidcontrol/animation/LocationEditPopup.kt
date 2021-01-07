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

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import animatedledstrip.androidcontrol.R
import animatedledstrip.leds.locationmanagement.Location

/**
 * Pops up to modify a location parameter
 */
class LocationEditPopup(
    private val initialValue: Location,
    private val paramName: String,
    private val frag: LocationSelect
) : DialogFragment() {

    private lateinit var listener: LocationEditListener
    private lateinit var textInX: EditText
    private lateinit var textInY: EditText
    private lateinit var textInZ: EditText

    interface LocationEditListener {
        fun onLocationDialogPositiveClick(
            dialog: DialogFragment, parameter: String,
            newValueX: String, newValueY: String, newValueZ: String,
            frag: LocationSelect
        )

        fun onLocationDialogNegativeClick(dialog: DialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        check(context is LocationEditListener)
        listener = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.popup_int_edit, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        checkNotNull(activity)
        return activity.let {
            textInX = EditText(this.context!!).apply {
                inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_CLASS_TEXT
                setText(initialValue.x.toString())
                layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                    setMargins(15, 0, 30, 0)
                }
            }
            textInY = EditText(this.context!!).apply {
                inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_CLASS_TEXT
                setText(initialValue.y.toString())
                layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                    setMargins(15, 0, 30, 0)
                }
            }
            textInZ = EditText(this.context!!).apply {
                inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_CLASS_TEXT
                setText(initialValue.z.toString())
                layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                    setMargins(15, 0, 30, 0)
                }
            }

            val container = LinearLayout(this.context!!).apply {
                orientation = LinearLayout.VERTICAL

                addView(LinearLayout(this.context!!).apply {
                    orientation = LinearLayout.HORIZONTAL
                    addView(TextView(this.context!!).apply {
                        text = "X: "
                        layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                            setMargins(30, 0, 15, 0)
                        }
                    })
                    addView(textInX)
                })
                addView(LinearLayout(this.context!!).apply {
                    orientation = LinearLayout.HORIZONTAL
                    addView(TextView(this.context!!).apply {
                        text = "Y: "
                        layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                            setMargins(30, 0, 15, 0)
                        }
                    })
                    addView(textInY)
                })
                addView(LinearLayout(this.context!!).apply {
                    orientation = LinearLayout.HORIZONTAL
                    addView(TextView(this.context!!).apply {
                        text = "Z: "
                        layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                            setMargins(30, 0, 15, 0)
                        }
                    })
                    addView(textInZ)
                })
            }

            AlertDialog.Builder(it)
                .setView(container)
                .setTitle(getString(R.string.popup_dialog_header_edit_number, paramName))
                .setPositiveButton(getString(R.string.popup_dialog_button_save)) { _, _ ->
                    listener.onLocationDialogPositiveClick(
                        this, paramName,
                        textInX.text.toString(),
                        textInY.text.toString(),
                        textInZ.text.toString(),
                        frag
                    )
                }
                .setNegativeButton(getString(R.string.popup_dialog_button_cancel)) { _, _ ->
                    listener.onLocationDialogNegativeClick(this)
                }
                .create()
        }
    }

}
