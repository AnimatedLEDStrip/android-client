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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.core.content.ContextCompat
import animatedledstrip.androidcontrol.animation.AnimationSelect
import animatedledstrip.androidcontrol.connections.AddConnectionActivity
import animatedledstrip.androidcontrol.connections.ConnectionFragment
import animatedledstrip.androidcontrol.settings.SettingsActivity
import animatedledstrip.androidcontrol.utils.*
import animatedledstrip.animationutils.AnimationData
import animatedledstrip.client.send
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), AnimationSelect.OnFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri) {
    }

    private val sharedPrefFile = "ledprefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter =
            TabAdapter(this, supportFragmentManager)
        val viewPager: androidx.viewpager.widget.ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(viewPager)
        setSupportActionBar(toolbar)


        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.INTERNET
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.INTERNET), 1)
        }

        mPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        AppCompatDelegate.setDefaultNightMode(
            when (mPreferences.getString(DARK_KEY, null)) {
                "Light" -> MODE_NIGHT_NO
                "Dark" -> MODE_NIGHT_YES
                else ->
                    if (android.os.Build.VERSION.SDK_INT >= 29) AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    else AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
            }
        )

        mainSender
            .setAsDefaultSender()
            .setOnConnectCallback { ip ->
                connected = true
                fab.backgroundTintList = ColorStateList.valueOf(Color.GREEN)
                fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_send))
                val ipFrag = supportFragmentManager.findFragmentByTag(ip) as ConnectionFragment?
                runOnUiThread {
                    supportActionBar?.title = "AnimatedLEDStrip ($ip)"
                    ipFrag?.connectButton?.text = getString(R.string.connected)
                }
            }.setOnDisconnectCallback { ip ->
                connected = false
                fab.backgroundTintList = ColorStateList.valueOf(Color.RED)
                fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_not_connected))
                val ipFrag = supportFragmentManager.findFragmentByTag(ip) as ConnectionFragment?
                runOnUiThread {
                    supportActionBar?.title = "AnimatedLEDStrip (Disconnected)"
                    mainSender.runningAnimations.forEach { (id, _) ->
                        supportFragmentManager.beginTransaction()
                            .remove(supportFragmentManager.findFragmentByTag(id) ?: return@forEach)
                            .commit()
                    }
                    ipFrag?.connectButton?.text = getString(R.string.disconnected)
                }
                mainSender.end()
            }.setOnReceiveCallback {
                Log.d("Server", it.toString())
            }

        fab.setOnClickListener {
            if (connected) {
                animationData.send()
                val selectFrag =
                    supportFragmentManager.findFragmentByTag("anim select") as AnimationSelect?
                selectFrag?.resetView() ?: Log.d("FAB", "Error")
            } else Toast.makeText(this, "Not Connected", Toast.LENGTH_SHORT).show()
        }
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
            R.id.action_add_ip -> {
                startActivity(Intent(this, AddConnectionActivity::class.java))
                true
            }
            R.id.action_disconnect -> {
                if (mainSender.connected)
                    mainSender.end()
                else Toast.makeText(this, "Not Connected", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_clear -> {
                if (mainSender.connected)
                    AnimationData().send()
                else Toast.makeText(this, "Not Connected", Toast.LENGTH_SHORT).show()
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