package animatedledstrip.androidcontrol.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.preference.ListPreference
import androidx.preference.Preference
import animatedledstrip.androidcontrol.R
import animatedledstrip.androidcontrol.utils.DARK_KEY
import animatedledstrip.androidcontrol.utils.mPreferences
import animatedledstrip.androidcontrol.utils.mainSender
import com.takisoft.fix.support.v7.preference.EditTextPreference
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat


class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootkey: String?) {
        EditTextPreference(this.context)
        setPreferencesFromResource(R.xml.preferences, rootkey)
        val darkPreference = findPreference("dark_setting") as ListPreference
        val portPreference = findPreference("port_setting") as EditTextPreference

        portPreference.text = mainSender.port.toString()

        val preferenceListener = Preference.OnPreferenceChangeListener { preference, value ->
            preference.summary = value as String
            val preferencesEditor: SharedPreferences.Editor = mPreferences.edit()
            when (preference) {
                darkPreference -> {
                    setDefaultNightMode(
                        when (value) {
                            "Light" -> MODE_NIGHT_NO
                            "Dark" -> MODE_NIGHT_YES
                            else ->
                                if (android.os.Build.VERSION.SDK_INT >= 29) MODE_NIGHT_FOLLOW_SYSTEM
                                else MODE_NIGHT_AUTO_BATTERY
                        }
                    )
                    preferencesEditor.putString(
                        DARK_KEY,
                        when (value) {
                            "Light" -> "Light"
                            "Dark" -> "Dark"
                            else -> "Default"
                        }
                    ).apply()
                }
                portPreference -> {
                    mainSender.setPort(value.toInt())
                }
            }
            true
        }

        portPreference.summary = portPreference.text

        darkPreference.onPreferenceChangeListener = preferenceListener
        portPreference.onPreferenceChangeListener = preferenceListener
    }
}