package animatedledstrip.androidcontrol

import android.content.SharedPreferences
import animatedledstrip.client.AnimationSenderFactory
import animatedledstrip.leds.AnimationData

var ip = "0.0.0.0"
const val IP_KEY = "ip"
var mainSender: AnimationSenderFactory.AnimationSender =
    AnimationSenderFactory.create(ipAddress = ip, port = 6, connectAttemptLimit = 1)
        .start()
        .setAsDefaultSender()

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