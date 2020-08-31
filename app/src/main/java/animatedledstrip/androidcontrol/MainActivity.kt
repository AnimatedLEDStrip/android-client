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
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import animatedledstrip.androidcontrol.animation.AnimationSelect
import animatedledstrip.androidcontrol.connections.AddConnectionActivity
import animatedledstrip.androidcontrol.connections.ConnectionFragment
import animatedledstrip.androidcontrol.settings.SettingsActivity
import animatedledstrip.androidcontrol.tabs.TabAdapter
import animatedledstrip.androidcontrol.utils.*
import animatedledstrip.animationutils.AnimationData
import animatedledstrip.client.send
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Starting point for the app
 */
class MainActivity : AppCompatActivity(), AnimationSelect.OnFragmentInteractionListener {

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
        mainSender
            .setAsDefaultSender()
            .setOnConnectCallback { ip, _ ->
                runOnUiThread {
                    tabAdapter.notifyDataSetChanged()
                }

                fab.backgroundTintList = ColorStateList.valueOf(Color.GREEN)
                fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_send))

                val ipFrag =
                    supportFragmentManager.findFragmentByTag(ip) as ConnectionFragment?
                runOnUiThread {
                    supportActionBar?.title = getString(R.string.title_activity_main_connected, ip)
                    ipFrag?.connectButton?.text = getString(R.string.server_list_button_connected)
                }

                mPreferences.edit().putString(RECENT_IP_KEY, ip).apply()
            }
            .setOnDisconnectCallback { ip, _ ->
                runOnUiThread {
                    tabAdapter.notifyDataSetChanged()
                }

                fab.backgroundTintList = ColorStateList.valueOf(Color.RED)
                fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_not_connected))

                val ipFrag =
                    supportFragmentManager.findFragmentByTag(ip) as ConnectionFragment?
                runOnUiThread {
                    supportActionBar?.title = getString(R.string.title_activity_main_disconnected)
                    mainSender.runningAnimations.forEach { (id, _) ->
                        supportFragmentManager.beginTransaction()
                            .remove(supportFragmentManager.findFragmentByTag(id) ?: return@forEach)
                            .commit()
                    }
                    ipFrag?.connectButton?.text =
                        getString(R.string.server_list_button_disconnected)
                }

                mainSender.end()

                runOnUiThread {
                    animationOptionAdapter.clear()
                }

                mPreferences.edit().putString(RECENT_IP_KEY, "").apply()
            }
            .setOnUnableToConnectCallback { ip, _ ->
                val ipFrag =
                    supportFragmentManager.findFragmentByTag(ip) as ConnectionFragment?
                runOnUiThread {
                    Toast.makeText(
                        this,
                        getString(R.string.toast_body_could_not_connect, ip),
                        Toast.LENGTH_SHORT
                    ).show()
                    ipFrag?.connectButton?.text =
                        getString(R.string.server_list_button_disconnected)
                }

                mPreferences.edit().putString(RECENT_IP_KEY, "").apply()
            }
            .setOnNewAnimationInfoCallback {
                runOnUiThread {
                    animationOptionAdapter.add(it.name)
                }
            }
    }

    private fun setFABOnClick() {
        fab.setOnClickListener {
            if (mainSender.connected) {
                animationData.send()
                val selectFrag =
                    supportFragmentManager.findFragmentByTag("anim select") as AnimationSelect?
                selectFrag?.resetView() ?: Log.d("FAB", "Error")
            } else Toast.makeText(
                this,
                getString(R.string.toast_body_not_connected),
                Toast.LENGTH_SHORT
            ).show()
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
        supportActionBar?.title = getString(R.string.title_activity_main_disconnected)

        checkPermissions()

        mPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        animationOptionAdapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                mutableListOf<String>()
            ).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }

        loadPreferences()
        setConnectionCallbacks()
        setFABOnClick()

        val recentIp = mPreferences.getString(RECENT_IP_KEY, "")!!

        if (recentIp.isNotEmpty())
            mainSender.setIPAddress(recentIp, start = true)
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
                startActivity(Intent(this, AddConnectionActivity::class.java))
                true
            }
            R.id.action_disconnect -> {
                if (mainSender.connected)
                    mainSender.end()
                else Toast.makeText(
                    this,
                    getString(R.string.toast_body_not_connected),
                    Toast.LENGTH_SHORT
                )
                    .show()
                true
            }
            R.id.action_clear -> {
                if (mainSender.connected)
                    AnimationData().send()
                else Toast.makeText(
                    this,
                    getString(R.string.toast_body_not_connected),
                    Toast.LENGTH_SHORT
                )
                    .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onFragmentInteraction(uri: Uri) {}
}
