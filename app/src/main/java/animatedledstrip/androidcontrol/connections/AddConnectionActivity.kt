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

package animatedledstrip.androidcontrol.connections

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import animatedledstrip.androidcontrol.R
import animatedledstrip.androidcontrol.utils.IP_KEY
import animatedledstrip.androidcontrol.utils.IPs
import animatedledstrip.androidcontrol.utils.mPreferences
import kotlinx.android.synthetic.main.activity_add_connection.*
import kotlinx.android.synthetic.main.content_add_connection.*

class AddConnectionActivity : AppCompatActivity(), ServerEditFragment.ServerEditListener {
    override fun onDialogPositiveClick(dialog: DialogFragment, oldIp: String, newIp: String) {
        val index = IPs.indexOf(oldIp)
        IPs.remove(oldIp)
        if (index == -1) IPs.add(newIp) else IPs.add(index, newIp)
        saveServerIPs()
        loadServers()
        Log.d("IPs", IPs.toString())
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {

    }

    override fun onRemoveClick(dialog: DialogFragment, ip: String) {
        IPs.remove(ip)
        saveServerIPs()
        loadServers()
    }

    private fun showEditDialog() {
        val dialog = ServerEditFragment("")
        dialog.show(supportFragmentManager, "ServerEditFragment")
    }

    private fun loadServers() {
        ip_list.removeAllViews()
        Log.d("IPs", "Load called")
        IPs.forEach { ip ->
            supportFragmentManager.beginTransaction()
                .add(
                    ip_list.id,
                    ServerFragment.newInstance(ip),
                    ip
                )
                .commit()
        }
    }

    private fun saveServerIPs() {
        mPreferences.edit().putStringSet(IP_KEY, IPs.toSet()).apply()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_connection)
        setSupportActionBar(toolbar)

        loadServers()
        fab.setOnClickListener {
            showEditDialog()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}
