/*
 *  Copyright (c) 2020 AnimatedLEDStrip
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package animatedledstrip.androidcontrol.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import animatedledstrip.androidcontrol.R
import animatedledstrip.androidcontrol.utils.DARK_KEY
import animatedledstrip.androidcontrol.utils.mPreferences
import animatedledstrip.androidcontrol.utils.setNightModeFromPreferences
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat

/**
 * Settings menu
 */
class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootkey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootkey)

        val darkPreference = findPreference("dark_setting") as ListPreference

        val preferenceListener = Preference.OnPreferenceChangeListener { preference, value ->
            val preferencesEditor: SharedPreferences.Editor = mPreferences.edit()
            when (preference) {
                darkPreference -> {
                    preference.summary = value as String
                    preferencesEditor.putString(
                        DARK_KEY,
                        when (value) {
                            "Light" -> "Light"
                            "Dark" -> "Dark"
                            else -> "Default"
                        }
                    ).apply()
                    setNightModeFromPreferences()
                }
            }
            true
        }

        darkPreference.summary = darkPreference.value.toString()

        darkPreference.onPreferenceChangeListener = preferenceListener
    }
}