package animatedledstrip.androidcontrol.animation

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import animatedledstrip.androidcontrol.R
import animatedledstrip.androidcontrol.utils.animationData
import animatedledstrip.animationutils.Animation
import animatedledstrip.animationutils.AnimationData
import animatedledstrip.animationutils.ReqLevel
import animatedledstrip.utils.getAnimationOrNull
import animatedledstrip.utils.info
import animatedledstrip.utils.infoOrNull
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
            parent === animation_list -> resetView()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    fun resetView() {
        animation_options.removeAllViews()
        animationData = AnimationData()
        addAnimationOptions(
            animation_list.selectedItem.toString().getAnimationOrNull()?.infoOrNull()
                ?: Animation.COLOR.info()
        )
    }

    private fun addAnimationOptions(info: animatedledstrip.animationutils.AnimationInfo) {
        animationData.animation = info.animation

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
