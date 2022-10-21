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

package animatedledstrip.androidcontrol

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import animatedledstrip.androidcontrol.animation.creation.AnimationCreation
import animatedledstrip.androidcontrol.animation.creation.ParamPopupListener
import animatedledstrip.androidcontrol.animation.creation.param.*
import animatedledstrip.androidcontrol.connections.AddServerActivity
import animatedledstrip.androidcontrol.connections.ServerFragment
import animatedledstrip.androidcontrol.settings.SettingsActivity
import animatedledstrip.androidcontrol.tabs.TabAdapter
import animatedledstrip.androidcontrol.utils.*
import animatedledstrip.animations.parameters.AbsoluteDistance
import animatedledstrip.animations.parameters.DegreesRotation
import animatedledstrip.animations.parameters.PercentDistance
import animatedledstrip.animations.parameters.RadiansRotation
import animatedledstrip.leds.animationmanagement.*
import animatedledstrip.leds.locationmanagement.Location
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_distance_select.*
import kotlinx.android.synthetic.main.fragment_double_select.*
import kotlinx.android.synthetic.main.fragment_id_select.*
import kotlinx.android.synthetic.main.fragment_int_select.*
import kotlinx.android.synthetic.main.fragment_location_select.*
import kotlinx.android.synthetic.main.fragment_rotation_select.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.ConnectException

/**
 * Starting point for the app
 */
class MainActivity : AppCompatActivity(),
    AnimationCreation.OnFragmentInteractionListener,
    ParamPopupListener {
    /**
     * Name of the preferences file
     */
    private val sharedPrefFile = "led_prefs"

    private lateinit var tabAdapter: TabAdapter

    /**
     * Check for permissions and ask for them if necessary
     */
    private fun checkPermissions() {
        if (
            ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
            != PackageManager.PERMISSION_GRANTED
        ) requestPermissions(arrayOf(Manifest.permission.INTERNET), 1)
    }

    private fun setConnectionCallbacks() {
        ConnectionEventActions.populateData = { ip ->
            val ipFrag =
                supportFragmentManager.findFragmentByTag(ip) as ServerFragment?
            runOnUiThread {
                supportActionBar?.title = getString(
                    R.string.title_activity_main_server_selected,
                    alsClientMap[ip]?.name ?: ip,
                )
                ipFrag?.selectButton?.text = getString(R.string.server_list_button_selected)
            }

            ioScope.launch(Dispatchers.IO) {
                try {
                    animationPropertyOptionAdapter.addAll(
                        alsClient?.getSupportedAnimationsNames() ?: return@launch
                    )
                    runOnUiThread {
                        tabAdapter.notifyDataSetChanged()
                    }
                } catch (e: ConnectException) {
                    resetIpAndClearData()
                }
            }

            mPreferences.edit().putString(RECENT_IP_KEY, ip).apply()
        }

        ConnectionEventActions.clearData = { ip ->
            val ipFrag =
                supportFragmentManager.findFragmentByTag(ip) as ServerFragment?
            runOnUiThread {
                supportActionBar?.title = getString(R.string.title_activity_main_no_server_selected)
                ipFrag?.selectButton?.text =
                    getString(R.string.server_list_button_not_selected)
            }

            runOnUiThread {
                animationPropertyOptionAdapter.clear()
                tabAdapter.notifyDataSetChanged()
            }

            mPreferences.edit().putString(RECENT_IP_KEY, "").apply()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tabAdapter =
            TabAdapter(
                this,
                supportFragmentManager
            )
        val viewPager: androidx.viewpager.widget.ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = tabAdapter
        tabs.setupWithViewPager(viewPager)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.title_activity_main_no_server_selected)

        checkPermissions()

        mPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        animationPropertyOptionAdapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                mutableListOf<String>()
            ).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }

        loadPreferences()
        setConnectionCallbacks()

        val recentIp = mPreferences.getString(RECENT_IP_KEY, "")!!

        if (recentIp.isNotEmpty()) {
            selectServerAndPopulateData(recentIp)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            R.id.action_add_ip -> {
                startActivity(Intent(this, AddServerActivity::class.java))
                true
            }
            R.id.action_clear -> {
                ioScope.launch(Dispatchers.IO) {
                    try {
                        alsClient?.clearStrip() ?: Toast.makeText(
                            this@MainActivity,
                            getString(R.string.toast_body_no_server_selected),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: ConnectException) {
                        resetIpAndClearData()
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onFragmentInteraction(uri: Uri) {}


    override fun onIntDialogPositiveClick(
        dialog: DialogFragment,
        parameter: String,
        newValue: String,
        frag: IntSelect
    ) {
        val newInt = newValue.toIntOrNull()
        if (newInt != null) {
            if (parameter == "Run Count") animParams.runCount = newInt
            else animParams.intParam(parameter, newInt)
            frag.int_param_value_text.text =
                getString(
                    R.string.run_anim_label_param,
                    parameter.camelToCapitalizedWords(),
                    if (parameter == "Run Count" && newValue == "-1" || newValue == "") "Endless"
                    else newInt.toString()
                )
        } else {
            Toast.makeText(
                this@MainActivity,
                getString(R.string.toast_body_invalid_parameter, newValue, parameter),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onIntDialogNegativeClick(dialog: DialogFragment) {}

    override fun onDoubleDialogPositiveClick(
        dialog: DialogFragment,
        parameter: String,
        newValue: String,
        frag: DoubleSelect
    ) {
        val newDouble = newValue.toDoubleOrNull()
        if (newDouble != null) animParams.doubleParam(parameter, newDouble)
        frag.double_param_value_text.text =
            getString(
                R.string.run_anim_label_param,
                parameter.camelToCapitalizedWords(),
                newDouble.toString()
            )
    }

    override fun onDoubleDialogNegativeClick(dialog: DialogFragment) {}

    override fun onIDDialogPositiveClick(
        dialog: DialogFragment,
        newValue: String,
        frag: IDSelect
    ) {
        animParams.id = newValue
        frag.id_param_value_text.text =
            getString(
                R.string.run_anim_label_id_param,
                newValue,
            )
    }

    override fun onIDDialogNegativeClick(dialog: DialogFragment) {}

    override fun onLocationDialogPositiveClick(
        dialog: DialogFragment,
        parameter: String,
        newValueX: String,
        newValueY: String,
        newValueZ: String,
        frag: LocationSelect,
    ) {
        val newLocation = Location(
            newValueX.toDoubleOrNull() ?: 0.0,
            newValueY.toDoubleOrNull() ?: 0.0,
            newValueZ.toDoubleOrNull() ?: 0.0,
        )
        animParams.locationParam(parameter, newLocation)
        frag.location_param_value_text.text =
            getString(
                R.string.run_anim_label_param,
                parameter.camelToCapitalizedWords(),
                newLocation.coordinates
            )
    }

    override fun onLocationDialogNegativeClick(dialog: DialogFragment) {}

    override fun onDistanceDialogPositiveClick(
        dialog: DialogFragment,
        parameter: String,
        newValueX: String,
        newValueY: String,
        newValueZ: String,
        isPercentDistance: Boolean,
        frag: DistanceSelect,
    ) {
        val newDistance = if (isPercentDistance)
            PercentDistance(
                newValueX.toDoubleOrNull() ?: 0.0,
                newValueY.toDoubleOrNull() ?: 0.0,
                newValueZ.toDoubleOrNull() ?: 0.0,
            )
        else
            AbsoluteDistance(
                newValueX.toDoubleOrNull() ?: 0.0,
                newValueY.toDoubleOrNull() ?: 0.0,
                newValueZ.toDoubleOrNull() ?: 0.0,
            )
        animParams.distanceParam(parameter, newDistance)
        frag.distance_param_value_text.text =
            getString(
                R.string.run_anim_label_param,
                parameter.camelToCapitalizedWords(),
                newDistance.coordinates
            )
    }

    override fun onDistanceDialogNegativeClick(dialog: DialogFragment) {}

    override fun onRotationDialogPositiveClick(
        dialog: DialogFragment,
        parameter: String,
        newValueX: String,
        newValueY: String,
        newValueZ: String,
        isDegreesRotation: Boolean,
        frag: RotationSelect,
    ) {
        val newRotation = if (isDegreesRotation)
            DegreesRotation(
                newValueX.toDoubleOrNull() ?: 0.0,
                newValueY.toDoubleOrNull() ?: 0.0,
                newValueZ.toDoubleOrNull() ?: 0.0,
            )
        else
            RadiansRotation(
                newValueX.toDoubleOrNull() ?: 0.0,
                newValueY.toDoubleOrNull() ?: 0.0,
                newValueZ.toDoubleOrNull() ?: 0.0,
            )
        animParams.rotationParam(parameter, newRotation)
        frag.rotation_param_value_text.text =
            getString(
                R.string.run_anim_label_param,
                parameter.camelToCapitalizedWords(),
                newRotation.let { "${it.xRotation}, ${it.yRotation}, ${it.zRotation} ${if (it is DegreesRotation) "deg" else "rad"}" }
            )
    }

    override fun onRotationDialogNegativeClick(dialog: DialogFragment) {}
}
