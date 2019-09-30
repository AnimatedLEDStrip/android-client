package animatedledstrip.androidcontrol.running


import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import animatedledstrip.androidcontrol.R
import animatedledstrip.utils.toARGB
import kotlinx.android.synthetic.main.fragment_animation_color.*

/**
 * A simple [Fragment] subclass.
 */
class AnimationColor(private val colors: List<Long>) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_animation_color, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        color_gradient.background = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            mutableListOf<Long>().apply {
                addAll(colors)
                add(colors[0])
            }.map { it.toARGB() }.toIntArray()
        )
    }

    companion object {
        @JvmStatic
        fun newInstance(colors: List<Long>) = AnimationColor(colors)
    }

}
