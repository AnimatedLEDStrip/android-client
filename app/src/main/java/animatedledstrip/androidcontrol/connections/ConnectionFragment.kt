package animatedledstrip.androidcontrol.connections


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import animatedledstrip.androidcontrol.R
import animatedledstrip.androidcontrol.utils.mainSender
import kotlinx.android.synthetic.main.fragment_connection.*

/**
 * A simple [Fragment] subclass.
 */
class ConnectionFragment(val name: String, private val ip: String) : Fragment() {

    lateinit var connectButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_connection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        server_name.text = ip
        connect_button.text =
            if (mainSender.ipAddress == ip && mainSender.connected) getString(R.string.connected)
            else getString(R.string.disconnected)
        connectButton = connect_button
        connect_button.setOnClickListener {
            if (mainSender.ipAddress == ip && mainSender.connected)
                mainSender.end()
            else {
                connectButton.text = getString(R.string.connecting)
                if (mainSender.ipAddress != ip) {
                    mainSender.setIPAddress(ip)         // Also starts connection
                } else if (!mainSender.connected)
                    mainSender.start()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(name: String, ip: String) = ConnectionFragment(name, ip)
    }

}
