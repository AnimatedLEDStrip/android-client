package animatedledstrip.androidcontrol.running


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import animatedledstrip.androidcontrol.R
import animatedledstrip.androidcontrol.utils.AnimationColor
import animatedledstrip.animationutils.AnimationData
import animatedledstrip.animationutils.ReqLevel
import animatedledstrip.animationutils.animationinfo.animationInfoMap
import animatedledstrip.client.endAnimation
import kotlinx.android.synthetic.main.fragment_animation.*

/**
 * A simple [Fragment] subclass.
 */
class AnimationFragment(private val data: AnimationData) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_animation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animation_id.text = data.id
        animation_name.text = getString(R.string.animation_label, data.animation.toString())
        animation_center.text = getString(R.string.center_label, data.center.toString())
        animation_continuous.text = getString(R.string.continuous_label, data.continuous.toString())
        animation_delay.text = getString(R.string.delay_label, data.delay.toString())
        animation_delay_mod.text = getString(R.string.delaymod_label, data.delayMod.toString())
        animation_direction.text = getString(R.string.direction_label, data.direction.toString())
        animation_distance.text = getString(R.string.distance_label, data.distance.toString())
        animation_spacing.text = getString(R.string.spacing_label, data.spacing.toString())

        animation_end.setOnClickListener {
            check(it is Button)
            data.endAnimation()
//            it.backgroundTintList = ColorStateList.valueOf(Color.GRAY)
            it.text = getString(R.string.ending_anim)
        }

        fun removeExcessData(view: View, reqLevel: ReqLevel) {
            if (reqLevel == ReqLevel.NOTUSED) animation_params.removeView(view)
        }

        data.colors.forEach {
            childFragmentManager.beginTransaction()
                .add(
                    animation_colors.id,
                    AnimationColor(it.toColorContainer().colors)
                )
                .commit()
        }

        val info = animationInfoMap[data.animation] ?: return

        removeExcessData(animation_center, info.center)
        removeExcessData(animation_delay, info.delay)
        removeExcessData(animation_delay_mod, info.delay)
        removeExcessData(animation_direction, info.direction)
        removeExcessData(animation_distance, info.distance)
        removeExcessData(animation_spacing, info.spacing)
        if (!info.repetitive) animation_params.removeView(animation_continuous)
    }


    companion object {
        @JvmStatic
        fun newInstance(data: AnimationData) = AnimationFragment(data)
    }
}
