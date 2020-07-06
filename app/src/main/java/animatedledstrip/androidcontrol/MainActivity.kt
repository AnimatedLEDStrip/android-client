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
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import animatedledstrip.androidcontrol.animation.AnimationSelect
import animatedledstrip.androidcontrol.connections.AddConnectionActivity
import animatedledstrip.androidcontrol.connections.ConnectionFragment
import animatedledstrip.androidcontrol.receivers.ClearStripBroadcastReceiver
import animatedledstrip.androidcontrol.receivers.DisconnectSenderBroadcastReceiver
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

    lateinit var tabAdapter: TabAdapter

    /**
     * Check for permissions and ask for them if necessary
     */
    private fun checkPermissions() {
        if (
            ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
            != PackageManager.PERMISSION_GRANTED
        ) requestPermissions(arrayOf(Manifest.permission.INTERNET), 1)
    }

    /**
     * Create the active connection notification channel
     */
    private fun createActiveConnectionNotificationChannel() {
        // Create the notification manager that will be used by the whole application
        notificationManager = NotificationManagerCompat.from(this)

        // Only add the channel if build version is Oreo or newer
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val activeConnectionChannel = NotificationChannel(
                channelID,
                getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = getString(R.string.notification_channel_description)
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(activeConnectionChannel)
        }
    }

    private fun setConnectionCallbacks() {
        val disconnectIntent = Intent(this, DisconnectSenderBroadcastReceiver::class.java)
        val clearIntent = Intent(this, ClearStripBroadcastReceiver::class.java)

        mainSender
            .setAsDefaultSender()
            .setOnConnectCallback { ip ->
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

                if (showNotification) with(notificationManager) {
                    notify(
                        activeNotificationId,
                        NotificationCompat.Builder(this@MainActivity, channelID)
                            .setSmallIcon(R.drawable.ic_led)
                            .setContentTitle(getString(R.string.notification_title))
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setContentIntent(
                                PendingIntent.getActivity(
                                    this@MainActivity,
                                    0,
                                    intent,
                                    0
                                )
                            )
                            .addAction(
                                R.drawable.ic_led,
                                getString(R.string.action_disconnect),
                                PendingIntent.getBroadcast(
                                    this@MainActivity,
                                    0,
                                    disconnectIntent,
                                    0
                                )
                            )
                            .addAction(
                                R.drawable.ic_led,
                                getString(R.string.action_clear_strip),
                                PendingIntent.getBroadcast(
                                    this@MainActivity,
                                    0,
                                    clearIntent,
                                    0
                                )
                            )
                            .setContentText(
                                getString(
                                    R.string.notification_body_connected_to,
                                    mainSender.ipAddress
                                )
                            )
                            .build()
                    )
                }
            }
            .setOnDisconnectCallback { ip ->
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

                cancelActiveNotification()

                runOnUiThread {
                    animationOptionAdapter.clear()
                }
            }
            .setOnReceiveCallback {
//                Log.d("Server", it)
            }
            .setOnDefinedAnimationCallback {
                runOnUiThread {
                    animationOptionAdapter.add(it.name)
                }
            }
            .setOnUnableToConnectCallback { ip ->
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
        createActiveConnectionNotificationChannel()

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