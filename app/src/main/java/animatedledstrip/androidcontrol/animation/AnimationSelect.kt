package animatedledstrip.androidcontrol.animation

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import animatedledstrip.androidcontrol.R
import animatedledstrip.androidcontrol.utils.animationData
import animatedledstrip.animationutils.Animation
import animatedledstrip.animationutils.AnimationData
import animatedledstrip.animationutils.AnimationInfo
import animatedledstrip.animationutils.ReqLevel
import animatedledstrip.animationutils.animationinfo.*


class AnimationSelect : Fragment(), AdapterView.OnItemSelectedListener {
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var animationSelect: Spinner
    lateinit var animationOptions: LinearLayout
    lateinit var animationOptionsScroll: ScrollView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val thisView = inflater.inflate(R.layout.fragment_animation_select, container, false)

        animationSelect = thisView.findViewById(R.id.animation_list)
        animationOptions = thisView.findViewById(R.id.animation_options)
        animationOptionsScroll = thisView.findViewById(R.id.animation_options_scroll)

        animationSelect.onItemSelectedListener = this

        return thisView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when {
            parent === animationSelect -> animationSelected(parent, position)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    private fun animationSelected(spinner: Spinner, pos: Int) {
        Log.d("AnimationSelect", "${spinner.getItemAtPosition(pos)}")

        animationOptions.removeAllViews()
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
                    animationOptions.id,
                    ContinuousSelect.newInstance()
                )
                .commit()

        for (i in 0 until info.numColors)
            childFragmentManager.beginTransaction()
                .add(
                    animationOptions.id,
                    ColorSelect.newInstance()
                )
                .commit()

        if (info.center != ReqLevel.NOTUSED)
            childFragmentManager.beginTransaction()
                .add(
                    animationOptions.id,
                    CenterSelect.newInstance()
                )
                .commit()

        if (info.distance != ReqLevel.NOTUSED)
            childFragmentManager.beginTransaction()
                .add(
                    animationOptions.id,
                    DistanceSelect.newInstance()
                )
                .commit()

        if (info.direction != ReqLevel.NOTUSED)
            childFragmentManager.beginTransaction()
                .add(
                    animationOptions.id,
                    DirectionSelect.newInstance()
                )
                .commit()

        if (info.spacing != ReqLevel.NOTUSED)
            childFragmentManager.beginTransaction()
                .add(
                    animationOptions.id,
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
