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
import androidx.fragment.app.DialogFragment
import animatedledstrip.androidcontrol.R

class ServerEditFragment(val ip: String) : DialogFragment() {

    lateinit var listener: ServerEditListener
    lateinit var textIn: EditText

    interface ServerEditListener {
        fun onDialogPositiveClick(dialog: DialogFragment, oldIp: String, newIp: String)
        fun onDialogNegativeClick(dialog: DialogFragment)
        fun onRemoveClick(dialog: DialogFragment, ip: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = context as ServerEditListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException("$context must implement ServerEditListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_server_edit, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            textIn = EditText(this.context!!).apply {
                inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_CLASS_TEXT
                setText(ip)
                layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                    setMargins(30, 0, 30, 0)
                }
            }
            val container = FrameLayout(this.context!!)
            container.addView(textIn)
            val builder = AlertDialog.Builder(it)
            builder.setView(container)
                .setTitle(if (ip == "") "New Server" else "Edit Server")
                .setPositiveButton("Save") { _, _ ->
                    listener.onDialogPositiveClick(
                        this,
                        ip,
                        textIn.text.toString()
                    )
                }
                .setNegativeButton("Cancel") { _, _ -> listener.onDialogNegativeClick(this) }
                .setNeutralButton("Remove") { _, _ ->
                    listener.onRemoveClick(
                        this, textIn.text.toString()
                    )
                }
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}
