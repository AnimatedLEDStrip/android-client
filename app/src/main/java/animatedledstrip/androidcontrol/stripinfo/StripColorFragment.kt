package animatedledstrip.androidcontrol.stripinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import animatedledstrip.androidcontrol.R
import animatedledstrip.androidcontrol.utils.alsClient
import animatedledstrip.androidcontrol.utils.ioScope
import animatedledstrip.androidcontrol.utils.resetIpAndClearData
import animatedledstrip.androidcontrol.views.ColorGradientViewer
import kotlinx.android.synthetic.main.fragment_strip_color.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.ConnectException

class StripColorFragment : Fragment() {
    private var dataRequester: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_strip_color, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataRequester = ioScope.launch(Dispatchers.IO) {
            while (true) {
                try {
                    val color = alsClient?.getCurrentStripColor() ?: continue
                    this@StripColorFragment.activity?.runOnUiThread {
                        try {
                            childFragmentManager.beginTransaction()
                                .replace(
                                    strip_color.id,
                                    ColorGradientViewer(color, false),
                                    "strip color"
                                )
                                .commit()
                        } catch (_: IllegalStateException) {
                        }
                    }
                } catch (e: ConnectException) {
                    resetIpAndClearData()
                }
            }
        }
    }

    override fun onDestroyView() {
        dataRequester?.cancel()
        super.onDestroyView()
    }
}