package animatedledstrip.androidcontrol.animation

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import animatedledstrip.androidcontrol.R
import animatedledstrip.androidcontrol.utils.animationData
import animatedledstrip.animationutils.Animation
import animatedledstrip.animationutils.AnimationData
import animatedledstrip.animationutils.AnimationInfo
import animatedledstrip.animationutils.ReqLevel
import animatedledstrip.animationutils.animationinfo.*
import kotlinx.android.synthetic.main.fragment_animation_select.*


class AnimationSelect : Fragment(), AdapterView.OnItemSelectedListener {
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_animation_select, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animation_list.onItemSelectedListener = this
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        check(context is OnFragmentInteractionListener)
        listener = context
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when {
            parent === animation_list -> animationSelected(parent, position)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    private fun animationSelected(spinner: Spinner, pos: Int) {
        Log.d("AnimationSelect", "${spinner.getItemAtPosition(pos)}")

        animation_options.removeAllViews()
        animationData = AnimationData()

        when (spinner.getItemAtPosition(pos)) {
            "Color" -> {
                animationData.animation = Animation.COLOR
                addAnimationOptions(AnimationInfo(numReqColors = 1))
            }
            "Bounce to Color" -> {
                animationData.animation = Animation.BOUNCETOCOLOR
                addAnimationOptions(BounceToColor)
            }
            "Multi-pixel Run to Color" -> {
                animationData.animation = Animation.MULTIPIXELRUNTOCOLOR
                addAnimationOptions(MultiPixelRunToColor)
            }
            "Sparkle to Color" -> {
                animationData.animation = Animation.SPARKLETOCOLOR
                addAnimationOptions(SparkleToColor)
            }
            "Splat" -> {
                animationData.animation = Animation.SPLAT
                addAnimationOptions(Splat)
            }
            "Stack" -> {
                animationData.animation = Animation.STACK
                addAnimationOptions(Stack)
            }
            "Wipe" -> {
                animationData.animation = Animation.WIPE
                addAnimationOptions(Wipe)
            }

            "Alternate" -> {
                animationData.animation = Animation.ALTERNATE
                addAnimationOptions(Alternate)
            }
            "Bounce" -> {
                animationData.animation = Animation.BOUNCE
                addAnimationOptions(Bounce)
            }
            "Meteor" -> {
                animationData.animation = Animation.METEOR
                addAnimationOptions(Meteor)
            }
            "Multi-pixel Run" -> {
                animationData.animation = Animation.MULTIPIXELRUN
                addAnimationOptions(MultiPixelRun)
            }
            "Pixel Marathon" -> {
                animationData.animation = Animation.PIXELMARATHON
                addAnimationOptions(PixelMarathon)
            }
            "Pixel Run" -> {
                animationData.animation = Animation.PIXELRUN
                addAnimationOptions(PixelRun)
            }
            "Ripple" -> {
                animationData.animation = Animation.RIPPLE
                addAnimationOptions(Ripple)
            }
            "Smooth Chase" -> {
                animationData.animation = Animation.SMOOTHCHASE
                addAnimationOptions(SmoothChase)
            }
            "Smooth Fade" -> {
                animationData.animation = Animation.SMOOTHFADE
                addAnimationOptions(SmoothFade)
            }
            "Sparkle" -> {
                animationData.animation = Animation.SPARKLE
                addAnimationOptions(Sparkle)
            }
            "Sparkle Fade" -> {
                animationData.animation = Animation.SPARKLEFADE
                addAnimationOptions(SparkleFade)
            }
            "StackOverflow" -> {
                animationData.animation = Animation.STACKOVERFLOW
                addAnimationOptions(StackOverflow)
            }
        }
    }

    private fun addAnimationOptions(info: animatedledstrip.animationutils.AnimationInfo) {
        if (info.repetitive)
            childFragmentManager.beginTransaction()
                .add(
                    animation_options.id,
                    ContinuousSelect.newInstance()
                )
                .commit()

        for (i in 0 until info.numColors)
            childFragmentManager.beginTransaction()
                .add(
                    animation_options.id,
                    ColorSelect.newInstance()
                )
                .commit()

        if (info.center != ReqLevel.NOTUSED)
            childFragmentManager.beginTransaction()
                .add(
                    animation_options.id,
                    CenterSelect.newInstance()
                )
                .commit()

        if (info.distance != ReqLevel.NOTUSED)
            childFragmentManager.beginTransaction()
                .add(
                    animation_options.id,
                    DistanceSelect.newInstance()
                )
                .commit()

        if (info.direction != ReqLevel.NOTUSED)
            childFragmentManager.beginTransaction()
                .add(
                    animation_options.id,
                    DirectionSelect.newInstance()
                )
                .commit()

        if (info.spacing != ReqLevel.NOTUSED)
            childFragmentManager.beginTransaction()
                .add(
                    animation_options.id,
                    SpacingSelect.newInstance()
                )
                .commit()


    }


    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance() = AnimationSelect()
    }
}
