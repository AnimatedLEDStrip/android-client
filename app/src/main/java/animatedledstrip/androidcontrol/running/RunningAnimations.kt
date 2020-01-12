package animatedledstrip.androidcontrol.running


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import animatedledstrip.androidcontrol.R
import animatedledstrip.androidcontrol.utils.mainSender
import kotlinx.android.synthetic.main.fragment_running_animations.*


class RunningAnimations : Fragment() {
    private fun addCurrentAnimations() {
        mainSender.runningAnimations.forEach { (id, data) ->
            fragmentManager?.beginTransaction()?.add(
                running_animation_list.id,
                AnimationFragment.newInstance(data),
                id
            )?.commit()
        }
    }

    private fun setConnectionCallbacks() {
        mainSender
            .setOnNewAnimationCallback { data ->
                activity?.runOnUiThread {
                    fragmentManager?.beginTransaction()?.add(
                        running_animation_list?.id ?: return@runOnUiThread,
                        AnimationFragment.newInstance(data),
                        data.id
                    )?.commit()
                }
            }.setOnEndAnimationCallback { data ->
                activity?.runOnUiThread {
                    fragmentManager?.beginTransaction()
                        ?.remove(
                            fragmentManager?.findFragmentByTag(data.id) ?: return@runOnUiThread
                        )
                        ?.commit()
                }
            }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_running_animations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addCurrentAnimations()
        setConnectionCallbacks()
    }

    companion object {
        @JvmStatic
        fun newInstance() = RunningAnimations()
    }

}
