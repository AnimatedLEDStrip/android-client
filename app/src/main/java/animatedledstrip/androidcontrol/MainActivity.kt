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
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import animatedledstrip.androidcontrol.animation.AnimationSelect
import animatedledstrip.androidcontrol.connections.AddConnectionActivity
import animatedledstrip.androidcontrol.connections.ConnectionFragment
import animatedledstrip.androidcontrol.settings.SettingsActivity
import animatedledstrip.androidcontrol.utils.*
import animatedledstrip.animationutils.AnimationData
import animatedledstrip.client.send
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), AnimationSelect.OnFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri) {
        Log.d("FragInt", "Activation")
    }

    private val sharedPrefFile = "ledprefs"

    private val fabOnClickListener = View.OnClickListener {

        when (connected) {
            false -> {

            }
            true -> {
                Log.d("FAB", "Sending $animationData")
                animationData.send()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter =
            TabAdapter(this, supportFragmentManager)
        val viewPager: androidx.viewpager.widget.ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(viewPager)
        val fab: FloatingActionButton = findViewById(R.id.fab)
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

        mainSender
            .setAsDefaultSender()
            .setOnConnectCallback { ip ->
                connected = true
                fab.backgroundTintList = ColorStateList.valueOf(Color.GREEN)
                fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_send))
                val ipFrag = supportFragmentManager.findFragmentByTag(ip) as ConnectionFragment
                runOnUiThread {
                    supportActionBar?.title = "AnimatedLEDStrip ($ip)"
                    ipFrag.connectButton.text = getString(R.string.connected)
                }
            }.setOnDisconnectCallback { ip ->
                connected = false
                fab.backgroundTintList = ColorStateList.valueOf(Color.RED)
                fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_not_connected))
                val ipFrag = supportFragmentManager.findFragmentByTag(ip) as ConnectionFragment
                runOnUiThread {
                    supportActionBar?.title = "AnimatedLEDStrip (Disconnected)"
                    mainSender.runningAnimations.forEach { (id, _) ->
                        supportFragmentManager.beginTransaction()
                            .remove(supportFragmentManager.findFragmentByTag(id) ?: return@forEach)
                            .commit()
                    }
                    ipFrag.connectButton.text = getString(R.string.disconnected)
                }
                mainSender.end()
            }.setOnReceiveCallback {
                Log.d("Server", it.toString())
            }

        fab.setOnClickListener(fabOnClickListener)
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
            R.id.action_add_ip -> {
                startActivity(Intent(this, AddConnectionActivity::class.java))
                true
            }
            R.id.action_disconnect -> {
                mainSender.end()
                true
            }
            R.id.action_clear -> {
                AnimationData().send()
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