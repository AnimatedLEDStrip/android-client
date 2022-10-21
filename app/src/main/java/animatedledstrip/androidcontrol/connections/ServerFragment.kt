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

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import animatedledstrip.androidcontrol.R
import animatedledstrip.androidcontrol.utils.alsClient
import animatedledstrip.androidcontrol.utils.resetIpAndClearData
import animatedledstrip.androidcontrol.utils.selectServerAndPopulateData
import kotlinx.android.synthetic.main.fragment_server.*

/**
 * Shows a single server, with a button for selecting it
 */
class ServerFragment(private val ip: String = "", private val name: String = "") : Fragment() {

    var selectButton: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_server, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        server_ip.text = ip
        if (name.isNotBlank()) {
            server_name.text = name
            server_name.setTypeface(null, Typeface.NORMAL)
        } else {
            server_name.text = getString(R.string.server_list_unnamed_server)
            server_name.setTypeface(null, Typeface.ITALIC)
        }
        select_button.text =
            if (alsClient?.ip == ip) getString(R.string.server_list_button_selected)
            else getString(R.string.server_list_button_not_selected)
        selectButton = select_button
        select_button.setOnClickListener {
            if (alsClient?.ip == ip) {
                resetIpAndClearData()
            } else {
                selectServerAndPopulateData(ip)
            }
        }
    }
}
