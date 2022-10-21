package animatedledstrip.androidcontrol.animation.select

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import animatedledstrip.androidcontrol.R
import animatedledstrip.androidcontrol.utils.alsClient
import animatedledstrip.androidcontrol.utils.camelToCapitalizedWords
import animatedledstrip.androidcontrol.utils.resetIpAndClearData
import animatedledstrip.androidcontrol.views.ColorGradientViewer
import animatedledstrip.leds.animationmanagement.AnimationToRunParams
import kotlinx.android.synthetic.main.fragment_animation_selection.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.net.ConnectException

class AnimationSelectionFragment(
    private val id: String = "",
    private val params: AnimationToRunParams
) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_animation_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animation_id.text = id
        animation_name.text =
            getString(R.string.run_anim_label_animation, params.animation)
        animation_run_count.text =
            getString(
                R.string.run_anim_label_run_count,
                when (params.runCount) {
                    -1 -> "Endless"
                    0 -> "Default"
                    else -> params.runCount.toString()
                }
            )

        fun newParamTextView(paramName: String, paramValue: String): TextView =
            TextView(this.requireContext()).apply {
                text = getString(R.string.run_anim_label_param, paramName, paramValue)
                setTextColor(resources.getColor(R.color.colorText, null))
            }

        for (param in params.intParams)
            animation_intParams.addView(
                newParamTextView(
                    param.key.camelToCapitalizedWords(),
                    param.value.toString()
                )
            )
        for (param in params.doubleParams)
            animation_doubleParams.addView(
                newParamTextView(
                    param.key.camelToCapitalizedWords(),
                    param.value.toString()
                )
            )
        for (param in params.locationParams)
            animation_locationParams.addView(
                newParamTextView(
                    param.key.camelToCapitalizedWords(),
                    "%.2f, %.2f, %.2f".format(param.value.x, param.value.y, param.value.z)
                )
            )
        for (param in params.distanceParams)
            animation_distanceParams.addView(
                newParamTextView(
                    param.key.camelToCapitalizedWords(),
                    "%.2f, %.2f, %.2f".format(param.value.x, param.value.y, param.value.z)
                )
            )
        for (param in params.rotationParams)
            animation_locationParams.addView(
                newParamTextView(
                    param.key.camelToCapitalizedWords(),
                    "%.2f, %.2f, %.2f (%s)".format(
                        param.value.xRotation,
                        param.value.yRotation,
                        param.value.zRotation,
                        param.value.rotationOrder.joinToString { "${it.name[0]}," })
                )
            )

        run_anim_button.setOnClickListener {
            runBlocking(Dispatchers.IO) {
                try {
                    alsClient?.startAnimation(id)
                } catch (e: ConnectException) {
                    resetIpAndClearData()
                }
            }
        }

        params.colors.forEach {
            childFragmentManager.beginTransaction()
                .add(animation_colors.id, ColorGradientViewer(it.toColorContainer(), true))
                .commit()
        }
    }
}