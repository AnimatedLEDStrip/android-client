package com.example.animatedledstripcontrol

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import animatedledstrip.androidclient.AndroidAnimationSenderFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ConnectingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val thisView = inflater.inflate(R.layout.fragment_connecting, container, false)

        connect()

        return thisView
    }

    fun connect() {
        GlobalScope.launch {
            Log.d("Socket", "Connecting")
            try {
                mainSender = AndroidAnimationSenderFactory.create(ipAddress = ip, port = 6, connectAttemptLimit = 20)
            } catch (e: Exception) {
                Log.d("Socket", "")
            }
            Log.d("Socket", "Connected to ${mainSender?.ipAddress}")
        }
    }

}
