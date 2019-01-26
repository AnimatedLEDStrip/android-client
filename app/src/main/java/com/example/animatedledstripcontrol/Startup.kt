package com.example.animatedledstripcontrol

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import animatedledstrip.androidclient.AndroidAnimationSenderFactory

import kotlinx.android.synthetic.main.activity_main.*

class Startup : AppCompatActivity() {

    private val sharedPrefFile = "ledprefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        mPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        ip = mPreferences.getString(IP_KEY, ip) ?: ip

        fab.backgroundTintList = ColorStateList.valueOf(Color.GRAY)

        fab.setOnClickListener {
            when (connected) {
                false -> {
                    fab.backgroundTintList = ColorStateList.valueOf(Color.YELLOW)
                    Log.d("Socket", "Connecting")
                    try {
                        mainSender =
                                AndroidAnimationSenderFactory.create(ipAddress = ip, port = 6, connectAttemptLimit = 1)
                                    .start()
                                    .setOnConnectCallback {
                                        fab.backgroundTintList = ColorStateList.valueOf(Color.GREEN)
                                        connected = true
                                    }
                                    .setOnDisconnectCallback {
                                        fab.backgroundTintList = ColorStateList.valueOf(Color.RED)
                                        connected = false
                                    }

                    } catch (e: Exception) {
                        Log.d("Socket", "Exception $e")
                        fab.backgroundTintList = ColorStateList.valueOf(Color.RED)
                    }
                    Log.d("Socket", "Connected to ${mainSender?.ipAddress}")

                }
                true -> {
                    mainSender?.end()
                    fab.backgroundTintList = ColorStateList.valueOf(Color.RED)
                }
            }
        }

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.startup_container, AnimationSelectFragment())
            .commit()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                openSettings()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openSettings() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

}
