package animatedledstrip.androidcontrol.animation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import animatedledstrip.androidcontrol.R
import animatedledstrip.androidcontrol.utils.animationData
import animatedledstrip.androidcontrol.utils.mainSender
import animatedledstrip.animationutils.center
import kotlinx.android.synthetic.main.fragment_center_select.*

/**
 * A simple [Fragment] subclass.
 */
class CenterSelect : Fragment(), SeekBar.OnSeekBarChangeListener {

    override fun onProgressChanged(bar: SeekBar?, progress: Int, p2: Boolean) {
        center_value.text = progress.toString()
        animationData.center(progress)
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_center_select, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        center_select.max = mainSender.stripInfo?.numLEDs ?: 240
        center_select.progress = center_select.max / 2
        center_select.setOnSeekBarChangeListener(this)
        center_value.text = center_select.progress.toString()
    }

    companion object {
        @JvmStatic
        fun newInstance() = CenterSelect()
    }

}
