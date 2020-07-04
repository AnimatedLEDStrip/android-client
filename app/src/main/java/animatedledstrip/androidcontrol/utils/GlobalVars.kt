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

package animatedledstrip.androidcontrol.utils

import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import animatedledstrip.animationutils.AnimationData
import animatedledstrip.client.AnimationSender

// Configured IPs
val IPs = mutableListOf<String>()

// Default port
const val defaultPort = 6

// Main Animation Sender
var mainSender: AnimationSender = AnimationSender(ipAddress = "", port = defaultPort)

// AnimationData instance that will be sent and recreated as needed
var animationData = AnimationData()

lateinit var animationOptionAdapter: ArrayAdapter<String>

fun LinearLayout?.indexOfChildOrNull(view: View?) = this?.indexOfChild(view)