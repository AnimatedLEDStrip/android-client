package animatedledstrip.androidcontrol.utils

import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import animatedledstrip.client.ALSHttpClient
import io.ktor.client.engine.android.*


// Keys for saving settings
const val DARK_KEY = "dark_mode"
const val IP_KEY = "ip_addrs"
const val NOTIFICATION_KEY = "do_notification"
const val RECENT_IP_KEY = "recent_ip"

// Loaded preferences
lateinit var mPreferences: SharedPreferences


fun loadPreferences() {
    if (mPreferences.getString(DARK_KEY, null) == null) saveDefaultPreferences()

    setNightModeFromPreferences()
    setIPsFromPreferences()
}

/**
 * Set dark mode setting
 */
fun setNightModeFromPreferences() {
    AppCompatDelegate.setDefaultNightMode(
        when (mPreferences.getString(DARK_KEY, null)) {
            "Light" -> AppCompatDelegate.MODE_NIGHT_NO
            "Dark" -> AppCompatDelegate.MODE_NIGHT_YES
            else ->
                if (Build.VERSION.SDK_INT >= 29) AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                else AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
        }
    )
}

fun setIPsFromPreferences() {
    alsClientMap.clear()
    val serverList = mPreferences.getStringSet(IP_KEY, null)?.toString() ?: ""

    for (server in serverList.split(",")) {
        val parsedServer =
            server.removePrefix("[")
                .removeSuffix("]")
                .removePrefix(" ")
                .split(";")
        val parsedIP = parsedServer[0]
        val parsedName = parsedServer.getOrElse(1) { "" }

        if (parsedIP != "") alsClientMap[parsedIP] = ALSClientInfo(
            client = ALSHttpClient(Android, parsedIP),
            ip = parsedIP,
            name = parsedName,
        )
    }
}

fun saveDefaultPreferences() {
    mPreferences.edit()
        .putString(DARK_KEY, "Default")
        .putString(RECENT_IP_KEY, "")
        .putBoolean(NOTIFICATION_KEY, true)
        .apply()
}
