package com.example.animatedledstripcontrol

import android.content.SharedPreferences
import animatedledstrip.androidclient.AndroidAnimationSenderFactory
import animatedledstrip.androidclient.AnimationData

var mainSender: AndroidAnimationSenderFactory.AndroidAnimationSender? = null
var ip = "0.0.0.0"
const val IP_KEY = "ip"
lateinit var mPreferences: SharedPreferences
var connected = false

var animationData = AnimationData()
var colorsSelected = 0

object AnimationNeeds {
    var numColors = 0
    var colorList = false
    var direction = false

    fun reset() {
        numColors = 0
        colorList = false
        direction = false
    }
}