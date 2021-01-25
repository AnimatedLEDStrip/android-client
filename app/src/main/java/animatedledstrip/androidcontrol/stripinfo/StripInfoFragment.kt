package animatedledstrip.androidcontrol.stripinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import animatedledstrip.androidcontrol.R
import animatedledstrip.androidcontrol.utils.alsClient
import kotlinx.android.synthetic.main.fragment_strip_info.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Shows information about the LED strip controlled by the connected server
 */
class StripInfoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_strip_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GlobalScope.launch(Dispatchers.IO) {
            val info = alsClient?.getStripInfo() ?: return@launch

            this@StripInfoFragment.activity?.runOnUiThread {
                num_leds.text =
                    getString(R.string.strip_info_num_leds, info.numLEDs.toString())
                pin.text =
                    getString(R.string.strip_info_pin, info.pin.toString())
                logging_enabled.text =
                    getString(R.string.strip_info_logging_enabled, info.isRenderLoggingEnabled.toString())
                if (!info.isRenderLoggingEnabled) {
                    strip_info_data.removeView(render_log_file_name)
                    strip_info_data.removeView(renders_between_log_saves)
                } else {
                    render_log_file_name.text =
                        getString(R.string.strip_info_log_file_name, info.renderLogFile)
                    renders_between_log_saves.text =
                        getString(
                            R.string.strip_info_renders_between_saves,
                            info.rendersBetweenLogSaves.toString()
                        )
                }
                is_1d_supported.text =
                    getString(R.string.strip_info_is_1d_supported, info.is1DSupported.toString())
                is_2d_supported.text =
                    getString(R.string.strip_info_is_2d_supported, info.is2DSupported.toString())
                is_3d_supported.text =
                    getString(R.string.strip_info_is_3d_supported, info.is3DSupported.toString())
            }

        }

    }
}
