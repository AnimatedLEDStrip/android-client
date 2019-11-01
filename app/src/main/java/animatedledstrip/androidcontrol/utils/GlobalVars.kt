package animatedledstrip.androidcontrol.utils

import android.content.SharedPreferences
import android.view.View
import android.widget.LinearLayout
import animatedledstrip.animationutils.AnimationData
import animatedledstrip.client.AnimationSenderFactory

const val DARK_KEY = "dark_mode"

// TODO: Save to disk instead of hard-coding
val IPs = mutableListOf(
    "10.44.36.53",
    "10.44.38.85",
    "10.44.157.2",
    "10.44.103.233"
)
var mainSender: AnimationSenderFactory.AnimationSender =
    AnimationSenderFactory.create(ipAddress = IPs[0], port = 6, connectAttemptLimit = 1)

lateinit var mPreferences: SharedPreferences
var connected = false

var animationData = AnimationData()

// TODO: Add more presets
val presetColors = listOf(
    listOf<Long>(
        0xFF0000,
        0xD52A00,
        0xAB5500,
        0xAB7F00,
        0xABAB00,
        0x56D500,
        0x00FF00,
        0x00D52A,
        0x00AB55,
        0x0056AA,
        0x0000FF,
        0x2A00D5,
        0x5500AB,
        0x7F0081,
        0xAB0055,
        0xD5002B
    )
)

fun LinearLayout?.indexOfChildOrNull(view: View?) = this?.indexOfChild(view)