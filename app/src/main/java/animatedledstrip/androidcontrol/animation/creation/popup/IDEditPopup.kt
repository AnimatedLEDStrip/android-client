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

package animatedledstrip.androidcontrol.animation.creation.popup

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
import android.widget.FrameLayout
import androidx.fragment.app.DialogFragment
import animatedledstrip.androidcontrol.R
import animatedledstrip.androidcontrol.animation.creation.param.IDSelect

/**
 * Pops up to modify a double parameter
 */
class IDEditPopup(
    private val initialValue: String?,
    private val frag: IDSelect,
) : DialogFragment() {

    private lateinit var listener: IDEditListener
    private lateinit var textIn: EditText

    interface IDEditListener {
        fun onIDDialogPositiveClick(
            dialog: DialogFragment,
            newValue: String,
            frag: IDSelect,
        )

        fun onIDDialogNegativeClick(dialog: DialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        check(context is IDEditListener)
        listener = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.popup_id_edit, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        requireActivity()
        return activity.let {
            textIn = EditText(this.requireContext()).apply {
                inputType = InputType.TYPE_CLASS_TEXT
                if (initialValue != null) setText(initialValue.toString())
                layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                    setMargins(30, 0, 30, 0)
                }
            }

            val container = FrameLayout(this.requireContext())
            container.addView(textIn)

            AlertDialog.Builder(it)
                .setView(container)
                .setTitle(getString(R.string.popup_dialog_header_edit_id))
                .setPositiveButton(getString(R.string.popup_dialog_button_save)) { _, _ ->
                    listener.onIDDialogPositiveClick(
                        this,
                        textIn.text.toString(),
                        frag,
                    )
                }
                .setNegativeButton(getString(R.string.popup_dialog_button_cancel)) { _, _ ->
                    listener.onIDDialogNegativeClick(this)
                }
                .create()
                .apply {
                    setOnShowListener {
                        getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                            resources.getColor(
                                R.color.colorText,
                                null
                            )
                        )
                        getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                            resources.getColor(
                                R.color.colorText,
                                null
                            )
                        )
                    }
                }
        }
    }

}
