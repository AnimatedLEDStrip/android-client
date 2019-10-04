package animatedledstrip.androidcontrol.animation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import animatedledstrip.androidcontrol.R
import kotlinx.android.synthetic.main.fragment_animation_select_container.*

/**
 * A simple [Fragment] subclass.
 */
class AnimationSelectContainer : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_animation_select_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentManager?.beginTransaction()?.add(
            anim_select_container.id,
            AnimationSelect.newInstance(),
            "anim select"
        )?.commit()
    }

    companion object {
        @JvmStatic
        fun newInstance() = AnimationSelectContainer()
    }
}
