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

import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import animatedledstrip.client.ALSHttpClient
import animatedledstrip.leds.animationmanagement.AnimationToRunParams
import io.ktor.client.engine.android.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.util.*

val alsClientMap: MutableMap<String, ALSClientInfo> = mutableMapOf()

fun MutableMap<String, ALSClientInfo>.toStringSet(): Set<String> =
    this.values.map { i -> "${i.ip};${i.name}" }.toSet()

var alsClient: ALSHttpClient<AndroidEngineConfig>? = null
    private set


// AnimationData instance that will be sent and recreated as needed
var animParams = AnimationToRunParams()

lateinit var animationPropertyOptionAdapter: ArrayAdapter<String>

fun LinearLayout?.indexOfChildOrNull(view: View?) = this?.indexOfChild(view)

/**
 * Adapted from https://stackoverflow.com/a/60010299/1944087
 */
fun String.camelToCapitalizedWords(): String {
    return "(?<=[a-zA-Z])[A-Z]".toRegex().replace(this) {
        " ${it.value}"
    }.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        .replace("-", "\n")
}

fun selectServerAndPopulateData(ip: String) {
    alsClient = alsClientMap[ip]?.client ?: run {
        Log.e("Select Server", "Server corresponding to ip $ip not found")
        return
    }
    ConnectionEventActions.populateData?.invoke(ip)
}

fun resetIpAndClearData() {
    val oldIp = alsClient?.ip ?: return
    alsClient = null
    ConnectionEventActions.clearData?.invoke(oldIp)
}

object ConnectionEventActions {
    var populateData: ((String) -> Any?)? = null
    var clearData: ((String) -> Any?)? = null
}

val ioScope = CoroutineScope(Dispatchers.IO)
