package com.example.animatedledstripcontrol


import android.content.res.ColorStateList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.Toast
import animatedledstrip.androidclient.AnimationData


class ColorSelectFragment : Fragment() {

    private lateinit var colorbuttoncontainer: LinearLayout
    private lateinit var selectedcolorsContainer: LinearLayout

    private val buttonMap = mutableMapOf<Button, Long>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val thisView = inflater.inflate(R.layout.fragment_color_select, container, false)

        colorbuttoncontainer = thisView.findViewById<View>(R.id.color_button_container) as LinearLayout


        selectedcolorsContainer = LinearLayout(this.context).apply {
            orientation = LinearLayout.HORIZONTAL
        }

        if (AnimationNeeds.numColors != 1) {

            val selectedColorsScroller = HorizontalScrollView(this.context)

            colorbuttoncontainer.addView(selectedColorsScroller)
            selectedColorsScroller.addView(selectedcolorsContainer)

            colorbuttoncontainer.addView(Button(this.context).apply {
                text = getString(R.string.send_colors)
                setOnClickListener {
                    if (AnimationNeeds.numColors != -1) try {
                        animationData.color1 = animationData.colorList[0]
                        animationData.color2 = animationData.colorList[1]
                        animationData.color3 = animationData.colorList[2]
                        animationData.color4 = animationData.colorList[3]
                        animationData.color5 = animationData.colorList[4]
                    } catch (e: IndexOutOfBoundsException) {
                    }

                    if (AnimationNeeds.direction) {
                        activity?.supportFragmentManager!!
                            .beginTransaction()
                            .replace(R.id.startup_container, DirectionSelectFragment())
                            .commit()
                    } else {
                        if (connected) {
                            animationData.send()
                        } else {
                            Toast.makeText(this.context, "Not connected to server", Toast.LENGTH_LONG).show()
                        }
                        animationData = AnimationData()
                        activity?.supportFragmentManager!!
                            .beginTransaction()
                            .replace(R.id.startup_container, AnimationSelectFragment())
                            .commit()
                    }
                }
            })
        }


        var buttonNumber = 1

        var currentRow: LinearLayout = LinearLayout(this.context).apply {
            orientation = LinearLayout.HORIZONTAL
        }
        colorbuttoncontainer.addView(currentRow)

        colorList.forEach { c ->
            val newButton = Button(this.context).apply {
                backgroundTintList = ColorStateList.valueOf(c.toInt())
                text = " "
                setOnClickListener {
                    val color = c and 0xFFFFFF
                    if (AnimationNeeds.numColors != 1 && (selectedcolorsContainer.childCount < AnimationNeeds.numColors || AnimationNeeds.numColors == -1)) {
                        animationData.colorList.add(color)
                        selectedcolorsContainer.addView(Button(this.context).apply {
                            backgroundTintList = ColorStateList.valueOf(c.toInt())
                            val color = c and 0xFFFFFF
                            setOnClickListener {
                                animationData.colorList.remove(color)
                                selectedcolorsContainer.removeView(this)
                                Log.d("ColorList", animationData.colorList.toString())
                            }
                        })
                    } else if (AnimationNeeds.numColors == 1) {
                        animationData.color1 = color
                        if (AnimationNeeds.numColors == 1) {
                            colorsSelected = 0

                            if (AnimationNeeds.direction) {
                                activity?.supportFragmentManager!!
                                    .beginTransaction()
                                    .replace(R.id.startup_container, DirectionSelectFragment())
                                    .commit()
                            } else {
                                if (connected) {
                                    animationData.send()
                                } else {
                                    Toast.makeText(this.context, "Not connected to server", Toast.LENGTH_LONG).show()
                                }
                                animationData = AnimationData()
                                AnimationNeeds.reset()
                                activity?.supportFragmentManager!!
                                    .beginTransaction()
                                    .replace(R.id.startup_container, AnimationSelectFragment())
                                    .commit()
                            }
                        }
                    }
                }
            }
            buttonMap[newButton] = c
            currentRow.addView(newButton)
            if (buttonNumber++ % 4 == 0) {
                currentRow = LinearLayout(this.context).apply {
                    orientation = LinearLayout.HORIZONTAL
                }
                colorbuttoncontainer.addView(currentRow)
            }
        }

        return thisView
    }


}
