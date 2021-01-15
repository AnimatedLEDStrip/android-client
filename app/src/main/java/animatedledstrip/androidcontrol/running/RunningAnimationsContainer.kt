/*
 *  Copyright (c) 2020 AnimatedLEDStrip
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package animatedledstrip.androidcontrol.running

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import animatedledstrip.androidcontrol.R
import animatedledstrip.androidcontrol.utils.mainSender
import animatedledstrip.client.send
import animatedledstrip.communication.Command
import kotlinx.android.synthetic.main.fragment_running_animations.*
import kotlinx.coroutines.*

/**
 * The list of running animations
 */
class RunningAnimationsContainer : Fragment() {

    /**
     * Add all animations that are running when this is created
     */
    private fun addCurrentRunningAnimations() {
        mainSender.runningAnimations.forEach { (id, data) ->
            parentFragmentManager.beginTransaction().add(
                running_animation_list.id,
                RunningAnimationFragment(data),
                id
            ).commit()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_running_animations, container, false)
    }

    private var dataRequester: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addCurrentRunningAnimations()

        dataRequester = GlobalScope.launch(Dispatchers.IO) {
            var lastList = mainSender.runningAnimations.toMap()
            while (mainSender.connected) {
                delay(500)
                val currentList = mainSender.runningAnimations.toMap()
                mainSender.runningAnimations.clear()
                Command("running list").send(mainSender)
                try {
                    parentFragmentManager.beginTransaction().apply {
                        lastList.keys.onEach { id ->
                            if (parentFragmentManager.findFragmentByTag(id) != null)
                                remove(parentFragmentManager.findFragmentByTag(id)!!)
                            else
                                Log.e("RA", "$id Not found")
                        }
                        currentList.onEach { (id, params) ->
                            add(
                                running_animation_list?.id ?: return@onEach,
                                RunningAnimationFragment(params),
                                id
                            )
                        }
                    }.commit()
                    lastList = currentList
                } catch (e: IllegalStateException) {
                }
            }
        }
    }

    override fun onDestroyView() {
        dataRequester?.cancel()
        super.onDestroyView()
    }
}
