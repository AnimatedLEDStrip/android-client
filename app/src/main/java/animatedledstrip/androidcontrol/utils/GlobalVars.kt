package animatedledstrip.androidcontrol.utils

import android.content.SharedPreferences
import animatedledstrip.animationutils.AnimationData
import animatedledstrip.client.AnimationSenderFactory

const val DARK_KEY = "dark_mode"

val IPs = listOf(
    "10.44.36.53",
    "10.44.38.85",
    "10.44.157.2"
)
var mainSender: AnimationSenderFactory.AnimationSender =
    AnimationSenderFactory.create(ipAddress = IPs[0], port = 6, connectAttemptLimit = 1)

lateinit var mPreferences: SharedPreferences
var connected = false

var animationData = AnimationData()

