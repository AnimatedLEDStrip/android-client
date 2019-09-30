package animatedledstrip.androidcontrol.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.preference.Preference
import animatedledstrip.androidcontrol.R
import animatedledstrip.androidcontrol.utils.IP_KEY
import animatedledstrip.androidcontrol.utils.ip
import animatedledstrip.androidcontrol.utils.mPreferences
import com.takisoft.fix.support.v7.preference.EditTextPreference
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat


class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootkey: String?) {
        EditTextPreference(this.context)
        setPreferencesFromResource(R.xml.preferences, rootkey)
        val ipPreference = findPreference("ip") as EditTextPreference

        ipPreference.text = ip

        val preferenceListener = Preference.OnPreferenceChangeListener { preference, value ->
            preference.summary = value as String
            val preferencesEditor: SharedPreferences.Editor = mPreferences.edit()
            when (preference) {
                ipPreference -> {
                    ip = value
                    preferencesEditor.putString(IP_KEY, value).apply()
                    Log.d("Settings", "Changed to $value $ip")
                }
            }
            true
        }

        ipPreference.summary = ipPreference.text

        ipPreference.onPreferenceChangeListener = preferenceListener
    }
}