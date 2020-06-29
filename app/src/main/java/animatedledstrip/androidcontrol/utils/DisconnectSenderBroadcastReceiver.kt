package animatedledstrip.androidcontrol.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class DisconnectSenderBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        mainSender.end()
    }

}