package animatedledstrip.androidcontrol

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.preference.Preference
import com.takisoft.fix.support.v7.preference.EditTextPreference
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat


class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootkey: String?) {
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
                }
            }
            true
        }

        ipPreference.summary = ipPreference.text

        ipPreference.onPreferenceChangeListener = preferenceListener
    }
}