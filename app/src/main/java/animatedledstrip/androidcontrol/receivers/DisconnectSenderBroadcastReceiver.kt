package animatedledstrip.androidcontrol.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import animatedledstrip.androidcontrol.utils.mainSender

class DisconnectSenderBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        mainSender.end()
    }

}