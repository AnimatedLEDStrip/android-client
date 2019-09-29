package animatedledstrip.androidcontrol.connections


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import animatedledstrip.androidcontrol.R
import animatedledstrip.androidcontrol.utils.mainSender
import kotlinx.android.synthetic.main.fragment_connection.*

/**
 * A simple [Fragment] subclass.
 */
class ConnectionFragment(val name: String, val ip: String) : Fragment() {

    private val disconnectedListener: (View) -> Unit = {
        mainSender.setIPAddress(ip)
        connect_button.setOnClickListener(connectedListener)
        Unit
    }

    private val connectedListener: (View) -> Unit = {
        mainSender.end()
        connect_button.setOnClickListener(disconnectedListener)
        Unit
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_connection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        server_name.text = name
        ip_address.text = ip
        connect_button.setOnClickListener(disconnectedListener)
    }

    companion object {
        @JvmStatic
        fun newInstance(name: String, ip: String) = ConnectionFragment(name, ip)
    }

}
