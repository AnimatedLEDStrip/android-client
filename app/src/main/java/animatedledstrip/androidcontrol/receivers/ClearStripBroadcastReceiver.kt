package animatedledstrip.androidcontrol.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import animatedledstrip.animationutils.AnimationData
import animatedledstrip.client.send

class ClearStripBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        AnimationData().send()
    }

}
