package animatedledstrip.androidcontrol.animation.select

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import animatedledstrip.androidcontrol.R
import animatedledstrip.androidcontrol.utils.alsClient
import animatedledstrip.androidcontrol.utils.ioScope
import animatedledstrip.androidcontrol.utils.resetIpAndClearData
import animatedledstrip.leds.animationmanagement.AnimationToRunParams
import kotlinx.android.synthetic.main.fragment_animation_selection_container.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.ConnectException

class AnimationSelectionContainer : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_animation_selection_container, container, false)
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        parentFragmentManager
//            .beginTransaction()
//            .add(
//                animations_list.id,
//                AnimationSelectionFragment(),
//                "anim select"
//            )
//            .commit()
//    }

    private var dataRequester: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataRequester = ioScope.launch(Dispatchers.IO) {
            var lastList = mapOf<String, AnimationToRunParams>()
            while (true) {
                try {
                    val currentList: Map<String, AnimationToRunParams> =
                        alsClient?.getSavedAnimations() ?: mapOf()
                    try {
                        parentFragmentManager.beginTransaction().apply {
                            lastList.keys.onEach { id ->
                                if (parentFragmentManager.findFragmentByTag(id) != null)
                                    remove(parentFragmentManager.findFragmentByTag(id)!!)
                                else
                                    Log.e("AL", "$id Not found")
                            }
                            currentList.onEach { (id, params) ->
                                add(
                                    animations_list?.id ?: return@onEach,
                                    AnimationSelectionFragment(id, params),
                                    id
                                )
                            }
                        }.commit()
                        lastList = currentList
                    } catch (_: IllegalStateException) {
                    }
                } catch (e: ConnectException) {
                    resetIpAndClearData()
                }
                delay(10000)
            }
        }
    }

    override fun onDestroyView() {
        dataRequester?.cancel()
        super.onDestroyView()
    }
}