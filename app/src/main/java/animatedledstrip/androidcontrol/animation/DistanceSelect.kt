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
import animatedledstrip.animationutils.ReqLevel
import animatedledstrip.animationutils.distance
import animatedledstrip.utils.infoOrNull
import kotlinx.android.synthetic.main.fragment_distance_select.*

class DistanceSelect : Fragment(), SeekBar.OnSeekBarChangeListener {

    override fun onProgressChanged(bar: SeekBar?, progress: Int, p2: Boolean) {
        distance_value.text = progress.toString()
        animationData.distance(progress)
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_distance_select, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        distance_select.max = mainSender.stripInfo?.numLEDs ?: 240
        val info = animationData.animation.infoOrNull()
        distance_select.progress =
            if (info?.distance != ReqLevel.NOTUSED && info?.distanceDefault != 0) info!!.distanceDefault
            else distance_select.max
        animationData.distance = distance_select.progress
        distance_select.setOnSeekBarChangeListener(this)
        distance_value.text = distance_select.progress.toString()
    }

    companion object {
        @JvmStatic
        fun newInstance() = DistanceSelect()
    }

}
