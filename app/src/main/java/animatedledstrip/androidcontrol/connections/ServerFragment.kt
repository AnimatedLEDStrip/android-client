package animatedledstrip.androidcontrol.connections


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import animatedledstrip.androidcontrol.R
import kotlinx.android.synthetic.main.fragment_server.*

/**
 * A simple [Fragment] subclass.
 */
class ServerFragment(var ip: String) : Fragment() {

    private fun showEditDialog() {
        val dialog = ServerEditFragment(ip)
        dialog.show(fragmentManager ?: return, "ServerEditFragment")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_server, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        server_name.text = ip
        server_edit.setOnClickListener {
            showEditDialog()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(ip: String) = ServerFragment(ip)
    }

}
