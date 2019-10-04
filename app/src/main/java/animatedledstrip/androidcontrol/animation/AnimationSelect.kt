package animatedledstrip.androidcontrol.animation

import android.content.Context
import android.net.Uri
import android.os.Bundle
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
import animatedledstrip.animationutils.animationinfo.animationInfoMap
import kotlinx.android.synthetic.main.fragment_animation_select.*


class AnimationSelect : Fragment(), AdapterView.OnItemSelectedListener {
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var spinner: Spinner
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_animation_select, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animation_list.onItemSelectedListener = this
        spinner = animation_list
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
            parent === animation_list -> resetView()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    private val animMap = mapOf(
        "Bounce to Color" to Animation.BOUNCETOCOLOR,
        "Multi-pixel Run To Color" to Animation.MULTIPIXELRUNTOCOLOR,
        "Sparkle to Color" to Animation.SPARKLETOCOLOR,
        "Splat" to Animation.SPLAT,
        "Stack" to Animation.STACK,
        "Wipe" to Animation.WIPE,
        "Alternate" to Animation.ALTERNATE,
        "Bounce" to Animation.BOUNCE,
        "Meteor" to Animation.METEOR,
        "Multi-pixel Run" to Animation.MULTIPIXELRUN,
        "Pixel Marathon" to Animation.PIXELMARATHON,
        "Pixel Run" to Animation.PIXELRUN,
        "Ripple" to Animation.RIPPLE,
        "Smooth Chase" to Animation.SMOOTHCHASE,
        "Smooth Fade" to Animation.SMOOTHFADE,
        "Sparkle" to Animation.SPARKLE,
        "Sparkle Fade" to Animation.SPARKLEFADE,
        "StackOverflow" to Animation.STACKOVERFLOW
    )

    fun resetView() {
        animation_options.removeAllViews()
        animationData = AnimationData()
        addAnimationOptions(getAnimInfo(spinner.selectedItem.toString()))
    }

    private fun getAnimInfo(item: String): animatedledstrip.animationutils.AnimationInfo {
        val animation = animMap[item] ?: Animation.COLOR
        animationData.animation = animation
        return if (animation != Animation.COLOR)
            animationInfoMap[animation] ?: AnimationInfo(numReqColors = 1)
        else
            AnimationInfo(numReqColors = 1)
    }

    private fun addAnimationOptions(info: animatedledstrip.animationutils.AnimationInfo) {

        fun addOptionFrag(frag: Fragment) {
            childFragmentManager
                .beginTransaction()
                .add(animation_options.id, frag)
                .commit()
        }

        if (info.repetitive) addOptionFrag(ContinuousSelect.newInstance())
        for (i in 0 until info.numColors) addOptionFrag(ColorSelect.newInstance())
        if (info.center != ReqLevel.NOTUSED) addOptionFrag(CenterSelect.newInstance())
        if (info.distance != ReqLevel.NOTUSED) addOptionFrag(DistanceSelect.newInstance())
        if (info.direction != ReqLevel.NOTUSED) addOptionFrag(DirectionSelect.newInstance())
        if (info.spacing != ReqLevel.NOTUSED) addOptionFrag(SpacingSelect.newInstance())
    }


    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance() = AnimationSelect()
    }
}
