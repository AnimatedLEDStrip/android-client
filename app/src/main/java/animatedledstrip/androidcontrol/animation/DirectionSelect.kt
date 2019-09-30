package animatedledstrip.androidcontrol.animation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import animatedledstrip.androidcontrol.R
import animatedledstrip.androidcontrol.utils.animationData
import animatedledstrip.animationutils.Direction
import animatedledstrip.animationutils.direction
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.fragment_direction_select.*

class DirectionSelect : androidx.fragment.app.Fragment() {

    lateinit var forwardButton: Button
    lateinit var backwardButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_direction_select, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        direction_toggle.setOnClickListener {
            check(it is Chip)
            when(it.text.toString()) {
                "Forward" -> {
                    animationData.direction(Direction.BACKWARD)
                    it.text = getString(R.string.backward)
                }
                "Backward" -> {
                    animationData.direction(Direction.FORWARD)
                    it.text = getString(R.string.forward)
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = DirectionSelect()
    }
}
