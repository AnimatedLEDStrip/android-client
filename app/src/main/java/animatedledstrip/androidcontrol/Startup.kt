package animatedledstrip.androidcontrol

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View

import kotlinx.android.synthetic.main.activity_main.*

class Startup : AppCompatActivity() {

    private val sharedPrefFile = "ledprefs"

    private val fabOnClickListener = View.OnClickListener {
        when (connected) {
            false -> {
                fab.backgroundTintList = ColorStateList.valueOf(Color.YELLOW)
                Log.d("Socket", "Connecting")
                try {
                    mainSender.setOnConnectCallback {
                        fab.backgroundTintList = ColorStateList.valueOf(Color.GREEN)
                        connected = true
                    }.setOnDisconnectCallback {
                        fab.backgroundTintList = ColorStateList.valueOf(Color.RED)
                        connected = false
                    }
                } catch (e: Exception) {
                    Log.d("Socket", "Exception $e")
                    fab.backgroundTintList = ColorStateList.valueOf(Color.RED)
                }
                Log.d("Socket", "Connected to ${mainSender.ipAddress}")

            }
            true -> {
                mainSender.end()
                fab.backgroundTintList = ColorStateList.valueOf(Color.RED)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.INTERNET
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.INTERNET), 1)
        }

        mPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        ip = mPreferences.getString(
            IP_KEY,
            ip
        ) ?: ip

        fab.backgroundTintList = ColorStateList.valueOf(if (connected) Color.GREEN else Color.GRAY)

        fab.setOnClickListener(fabOnClickListener)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.startup_container, AnimationSelectFragment())
            .commit()

    }

    override fun onPause() {
        super.onPause()
        mainSender.end()
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
