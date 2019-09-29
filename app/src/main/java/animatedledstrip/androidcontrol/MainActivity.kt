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
import animatedledstrip.androidcontrol.settings.SettingsActivity
import animatedledstrip.androidcontrol.utils.TabAdapter
import animatedledstrip.androidcontrol.utils.*
import animatedledstrip.client.send
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
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
        val tabs: TabLayout = findViewById(R.id.tabs)
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
            .setOnConnectCallback {
                connected = true
                fab.backgroundTintList = ColorStateList.valueOf(Color.GREEN)
                fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_send))
            }.setOnDisconnectCallback {
                connected = false
                fab.backgroundTintList = ColorStateList.valueOf(Color.RED)
                fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_not_connected))
                mainSender.end()
            }.setOnReceiveCallback {
                Log.d("Server", it.toString())
            }
            .start()

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