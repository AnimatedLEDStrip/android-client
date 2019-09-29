package animatedledstrip.androidcontrol.connections


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import animatedledstrip.androidcontrol.R
import kotlinx.android.synthetic.main.fragment_connect.*

/**
 * A simple [Fragment] subclass.
 */
class ConnectFragment : Fragment() {

    val piIPs = listOf(
        "10.44.36.53",
        "10.44.38.85",
        "10.44.157.2"
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_connect, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        piIPs.forEachIndexed { index, ip ->
            Log.d("Server", "$index")
            fragmentManager?.beginTransaction()
                ?.add(
                    connections.id,
                    ConnectionFragment.newInstance("Server $index", ip)
                )
                ?.commit()
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = ConnectFragment()
    }

}
