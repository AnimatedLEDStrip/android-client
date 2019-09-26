package animatedledstrip.androidcontrol.animation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import animatedledstrip.androidcontrol.R
import animatedledstrip.androidcontrol.utils.animationData

class ContinuousSelect : Fragment() {

    private lateinit var continuousAnimation: CheckBox

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val thisView = inflater.inflate(R.layout.fragment_continuous_select, container, false)

        continuousAnimation = thisView.findViewById(R.id.continuous_animation)
        continuousAnimation.isChecked = true

        continuousAnimation.setOnCheckedChangeListener { _, isChecked ->
            animationData.continuous = isChecked
        }

        return thisView
    }

    companion object {
        @JvmStatic
        fun newInstance() = ContinuousSelect()
    }

}
