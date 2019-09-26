package animatedledstrip.androidcontrol.utils

import android.content.SharedPreferences
import animatedledstrip.animationutils.AnimationData
import animatedledstrip.client.AnimationSenderFactory

var ip = "10.0.0.91"
const val IP_KEY = "ip"
var mainSender: AnimationSenderFactory.AnimationSender =
    AnimationSenderFactory.create(ipAddress = ip, port = 6, connectAttemptLimit = 1)

lateinit var mPreferences: SharedPreferences
var connected = false

var animationData = AnimationData()

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