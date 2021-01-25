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
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import animatedledstrip.androidcontrol.R
import animatedledstrip.androidcontrol.utils.IP_KEY
import animatedledstrip.androidcontrol.utils.alsClientMap
import animatedledstrip.androidcontrol.utils.mPreferences
import animatedledstrip.client.ALSHttpClient
import io.ktor.client.engine.android.*
import kotlinx.android.synthetic.main.activity_add_connection.*
import kotlinx.android.synthetic.main.content_add_connection.*

/**
 * Add, edit and remove servers from the list shown in the connections list
 */
class AddConnectionActivity : AppCompatActivity(), ServerEditPopup.ServerEditListener {
    override fun onDialogPositiveClick(dialog: DialogFragment, oldIp: String, newIp: String) {
        alsClientMap.remove(oldIp)
        alsClientMap[newIp] = ALSHttpClient(Android, newIp)
        saveServerIPs()
        loadServers()
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {

    }

    override fun onRemoveClick(dialog: DialogFragment, ip: String) {
        alsClientMap.remove(ip)
        saveServerIPs()
        loadServers()
    }

    private fun showEditDialog() {
        val dialog = ServerEditPopup("")
        dialog.show(supportFragmentManager, "ServerEditFragment")
    }

    private fun loadServers() {
        ip_list.removeAllViews()
        alsClientMap.forEach { (ip, _) ->
            supportFragmentManager.beginTransaction()
                .add(
                    ip_list.id,
                    ServerFragment(ip),
                    ip
                )
                .commit()
        }
    }

    private fun saveServerIPs() {
        mPreferences.edit().putStringSet(IP_KEY, alsClientMap.keys.toSet()).apply()
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
