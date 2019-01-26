package com.example.animatedledstripcontrol

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button


class AnimationSelectFragment : Fragment() {

    private lateinit var colorButton: Button
    private lateinit var multicolorButton: Button
    private lateinit var bouncetocolorButton: Button
    private lateinit var multipixelruntocolorButton: Button
    private lateinit var sparkletocolorButton: Button
    private lateinit var stackButton: Button
    private lateinit var wipeButton: Button
    private lateinit var alternateButton: Button
    private lateinit var bounceButton: Button
    private lateinit var multipixelrunButton: Button
    private lateinit var pixelmarathonButton: Button
    private lateinit var pixelrunButton: Button
    private lateinit var pixelrunwithtrailButton: Button
    private lateinit var smoothchaseButton: Button
    private lateinit var smoothfadeButton: Button
    private lateinit var sparkleButton: Button
    private lateinit var sparklefadeButton: Button
    private lateinit var stackoverflowButton: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val thisView = inflater.inflate(R.layout.fragment_animation_select, container, false)

        colorButton = thisView.findViewById<View>(R.id.color_button) as Button
        multicolorButton = thisView.findViewById<View>(R.id.multicolor_button) as Button
        bouncetocolorButton = thisView.findViewById<View>(R.id.bouncetocolor_button) as Button
        multipixelruntocolorButton = thisView.findViewById<View>(R.id.multipixelruntocolor_button) as Button
        sparkletocolorButton = thisView.findViewById<View>(R.id.sparkletocolor_button) as Button
        stackButton = thisView.findViewById<View>(R.id.stack_button) as Button
        wipeButton = thisView.findViewById<View>(R.id.wipe_button) as Button
        alternateButton = thisView.findViewById<View>(R.id.alternate_button) as Button
        bounceButton = thisView.findViewById<View>(R.id.bounce_button) as Button
        multipixelrunButton = thisView.findViewById<View>(R.id.multipixelrun_button) as Button
        pixelmarathonButton = thisView.findViewById<View>(R.id.pixelmarathon_button) as Button
        pixelrunButton = thisView.findViewById<View>(R.id.pixelrun_button) as Button
        pixelrunwithtrailButton = thisView.findViewById<View>(R.id.pixelrunwithtrail_button) as Button
        smoothchaseButton = thisView.findViewById<View>(R.id.smoothchase_button) as Button
        smoothfadeButton = thisView.findViewById<View>(R.id.smoothfade_button) as Button
        sparkleButton = thisView.findViewById<View>(R.id.sparkle_button) as Button
        sparklefadeButton = thisView.findViewById<View>(R.id.sparklefade_button) as Button
        stackoverflowButton = thisView.findViewById<View>(R.id.stackoverflow_button) as Button


        colorButton.setOnClickListener { setAnimation(colorButton) }
        multicolorButton.setOnClickListener { setAnimation(multicolorButton) }
        bouncetocolorButton.setOnClickListener { setAnimation(bouncetocolorButton) }
        multipixelruntocolorButton.setOnClickListener { setAnimation(multipixelruntocolorButton) }
        sparkletocolorButton.setOnClickListener { setAnimation(sparkletocolorButton) }
        stackButton.setOnClickListener { setAnimation(stackButton) }
        wipeButton.setOnClickListener { setAnimation(wipeButton) }
        alternateButton.setOnClickListener { setAnimation(alternateButton) }
        bounceButton.setOnClickListener { setAnimation(bounceButton) }
        multipixelrunButton.setOnClickListener { setAnimation(multipixelrunButton) }
        pixelmarathonButton.setOnClickListener { setAnimation(pixelmarathonButton) }
        pixelrunButton.setOnClickListener { setAnimation(pixelrunButton) }
        pixelrunwithtrailButton.setOnClickListener { setAnimation(pixelrunwithtrailButton) }
        smoothchaseButton.setOnClickListener { setAnimation(smoothchaseButton) }
        smoothfadeButton.setOnClickListener { setAnimation(smoothfadeButton) }
        sparkleButton.setOnClickListener { setAnimation(sparkleButton) }
        sparklefadeButton.setOnClickListener { setAnimation(sparklefadeButton) }
        stackoverflowButton.setOnClickListener { setAnimation(stackoverflowButton) }

        return thisView
    }

    fun setAnimation(view: View) {
        when (view) {
            colorButton -> {
                animationData.animation("COL")
                AnimationNeeds.numColors = 1
            }
            multicolorButton -> {
                animationData.animation("MCOL")
                AnimationNeeds.numColors = -1
            }
            bouncetocolorButton -> {
                animationData.animation("BTC")
                AnimationNeeds.numColors = 1
            }
            multipixelruntocolorButton -> {
                animationData.animation("MTC")
                AnimationNeeds.numColors = 1
                AnimationNeeds.direction = true
            }
            sparkletocolorButton -> {
                animationData.animation("STC")
                AnimationNeeds.numColors = 1
            }
            stackButton -> {
                animationData.animation("STK")
                AnimationNeeds.numColors = 1
                AnimationNeeds.direction = true
            }
            wipeButton -> {
                animationData.animation("WIP")
                AnimationNeeds.numColors = 1
                AnimationNeeds.direction = true
            }
            alternateButton -> {
                animationData.animation("ALT")
                AnimationNeeds.numColors = 2
            }
            bounceButton -> {
                animationData.animation("BNC")
                AnimationNeeds.numColors = 1
            }
            multipixelrunButton -> {
                animationData.animation("MPR")
                AnimationNeeds.numColors = 1
                AnimationNeeds.direction = true
            }
            pixelmarathonButton -> {
                animationData.animation("PXM")
                AnimationNeeds.numColors = 5
            }
            pixelrunButton -> {
                animationData.animation("PXR")
                AnimationNeeds.numColors = 1
                AnimationNeeds.direction = true
            }
            pixelrunwithtrailButton -> {
                animationData.animation("PXRT")
                AnimationNeeds.numColors = 1
                AnimationNeeds.direction = true
            }
            smoothchaseButton -> {
                animationData.animation("SCH")
                AnimationNeeds.numColors = -1
                AnimationNeeds.direction = true
            }
            smoothfadeButton -> {
                animationData.animation("SMF")
                AnimationNeeds.numColors = -1
                AnimationNeeds.direction = true
            }
            sparkleButton -> {
                animationData.animation("SPK")
                AnimationNeeds.numColors = 1
            }
            sparklefadeButton -> {
                animationData.animation("SPF")
                AnimationNeeds.numColors = 1
            }
            stackoverflowButton -> {
                animationData.animation("STO")
                AnimationNeeds.numColors = 2
            }
        }

        activity?.supportFragmentManager!!
            .beginTransaction()
            .replace(R.id.startup_container, ColorSelectFragment())
            .commit()
    }
}
