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

package animatedledstrip.androidcontrol.connections

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
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import animatedledstrip.androidcontrol.R

/**
 * Pops up to create, edit or remove a server from the list.
 */
class ServerEditPopup(private val ip: String, private val name: String) : DialogFragment() {

    private lateinit var listener: ServerEditListener
    private lateinit var ipTextIn: EditText
    private lateinit var nameTextIn: EditText

    interface ServerEditListener {
        fun onDialogPositiveClick(
            dialog: DialogFragment,
            oldIp: String,
            newIp: String,
            newName: String,
        )

        fun onDialogNegativeClick(dialog: DialogFragment)
        fun onRemoveClick(dialog: DialogFragment, ip: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        check(context is ServerEditListener)
        listener = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.popup_server_edit, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        requireActivity()
        return activity.let {
            nameTextIn = EditText(this.requireContext()).apply {
                hint = "Name"
                inputType = InputType.TYPE_CLASS_TEXT
                setText(name)
                layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                    setMargins(30, 0, 30, 0)
                }
            }
            ipTextIn = EditText(this.requireContext()).apply {
                hint = "IP Address"
                inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_CLASS_TEXT
                setText(ip)
                layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                    setMargins(30, 0, 30, 0)
                }
            }
            val inputLayout = LinearLayout(this.requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                addView(nameTextIn)
                addView(ipTextIn)
                layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            }

            val container = FrameLayout(this.requireContext())
            container.addView(inputLayout)

            AlertDialog.Builder(it)
                .setView(container)
                .setTitle(
                    if (ip == "") getString(R.string.popup_dialog_header_new_server)
                    else getString(R.string.popup_dialog_header_edit_server)
                )
                .setPositiveButton(getString(R.string.popup_dialog_button_save)) { _, _ ->
                    listener.onDialogPositiveClick(
                        this,
                        ip,
                        ipTextIn.text.toString(),
                        nameTextIn.text.toString(),
                    )
                }
                .setNegativeButton(getString(R.string.popup_dialog_button_cancel)) { _, _ ->
                    listener.onDialogNegativeClick(this)
                }
                .setNeutralButton(getString(R.string.popup_dialog_button_remove)) { _, _ ->
                    listener.onRemoveClick(this, ipTextIn.text.toString())
                }
                .create()
        }
    }

}
