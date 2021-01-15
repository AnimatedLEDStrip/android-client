package animatedledstrip.androidcontrol.stripinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import animatedledstrip.androidcontrol.R
import animatedledstrip.androidcontrol.utils.mainSender
import animatedledstrip.androidcontrol.views.ColorGradientViewer
import animatedledstrip.client.send
import animatedledstrip.communication.Command
import kotlinx.android.synthetic.main.fragment_strip_color.*
import kotlinx.coroutines.*

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

        mainSender.setOnNewCurrentStripColorCallback {
            this.activity?.runOnUiThread {
                try {
                    childFragmentManager.beginTransaction()
                        .replace(
                            strip_color.id,
                            ColorGradientViewer(it.color, false),
                            "strip color"
                        )
                        .commit()
                } catch (e: IllegalStateException) {
                }
            }
        }
        dataRequester = GlobalScope.launch(Dispatchers.IO) {
            while (true) {
                Command("strip color").send(mainSender)
                delay(100)
            }
        }
    }

    override fun onDestroyView() {
        dataRequester?.cancel()
        super.onDestroyView()
    }
}