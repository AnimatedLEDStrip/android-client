package animatedledstrip.androidcontrol.utils

import android.content.SharedPreferences
import android.view.View
import android.widget.LinearLayout
import animatedledstrip.animationutils.AnimationData
import animatedledstrip.client.AnimationSenderFactory
import animatedledstrip.colors.ccpresets.*

const val DARK_KEY = "dark_mode"
const val IP_KEY = "ip_addrs"
const val PORT_KEY = "port_sel"

val IPs = mutableListOf<String>()
const val defaultPort = 6
var mainSender: AnimationSenderFactory.AnimationSender =
    AnimationSenderFactory.create(ipAddress = "", port = defaultPort)

lateinit var mPreferences: SharedPreferences
var connected = false

var animationData = AnimationData()

val presetColors = listOf(
    listOf<Long>(                   // RainbowColors from FastLED
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
    ),
    listOf(                         // CloudColors from FastLED
        CCBlue.color,
        CCDarkBlue.color,
        CCDarkBlue.color,
        CCDarkBlue.color,
        CCDarkBlue.color,
        CCDarkBlue.color,
        CCDarkBlue.color,
        CCDarkBlue.color,
        CCBlue.color,
        CCDarkBlue.color,
        CCSkyBlue.color,
        CCSkyBlue.color,
        CCLightBlue.color,
        CCWhite.color,
        CCLightBlue.color,
        CCSkyBlue.color
    ),
    listOf(                         // LavaColors from FastLED
        CCBlack.color,
        CCMaroon.color,
        CCBlack.color,
        CCMaroon.color,
        CCDarkRed.color,
        CCMaroon.color,
        CCDarkRed.color,
        CCDarkRed.color,
        CCDarkRed.color,
        CCRed.color,
        CCOrange.color,
        CCWhite.color,
        CCOrange.color,
        CCRed.color,
        CCDarkRed.color
    ),
    listOf(                         // OceanColors from FastLED
        CCMidnightBlue.color,
        CCDarkBlue.color,
        CCMidnightBlue.color,
        CCNavy.color,
        CCDarkBlue.color,
        CCMediumBlue.color,
        CCSeaGreen.color,
        CCTeal.color,
        CCCadetBlue.color,
        CCBlue.color,
        CCDarkCyan.color,
        CCCornflowerBlue.color,
        CCAquamarine.color,
        CCSeaGreen.color,
        CCAqua.color,
        CCLightSkyBlue.color
    ),
    listOf(                         // ForestColors from FastLED
        CCDarkGreen.color,
        CCDarkGreen.color,
        CCDarkOliveGreen.color,
        CCDarkGreen.color,
        CCGreen.color,
        CCForestGreen.color,
        CCOliveDrab.color,
        CCGreen.color,
        CCSeaGreen.color,
        CCMediumAquamarine.color,
        CCLimeGreen.color,
        CCLawnGreen.color,
        CCMediumAquamarine.color,
        CCForestGreen.color
    ),
    listOf<Long>(                  // CCRainbowStripesColors from FastLED
        0xFF0000,
        0x000000,
        0xAB5500,
        0x000000,
        0xABAB00,
        0x000000,
        0x00FF00,
        0x000000,
        0x00AB55,
        0x000000,
        0x0000FF,
        0x000000,
        0x5500AB,
        0x000000,
        0xAB0055,
        0x000000
    ),
    listOf<Long>(                  // PartyColors from FastLED
        0x5500AB,
        0x84007C,
        0xB5004B,
        0xE5001B,
        0xE81700,
        0xB84700,
        0xAB7700,
        0xABAB00,
        0xAB5500,
        0xDD2200,
        0xF2000E,
        0xC2003E,
        0x8F0071,
        0x5F00A1,
        0x2F00D0,
        0x0007F9
    )
)

fun LinearLayout?.indexOfChildOrNull(view: View?) = this?.indexOfChild(view)