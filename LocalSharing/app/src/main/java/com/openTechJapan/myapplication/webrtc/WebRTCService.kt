package com.openTechJapan.myapplication.webrtc

import android.content.Context
import org.webrtc.PeerConnectionFactory

class WebRTCService {
    fun initializeOption(context: Context) {
        val options = PeerConnectionFactory.InitializationOptions
            .builder(context)
            .createInitializationOptions()
        PeerConnectionFactory.initialize(options)
    }

    // 3. PeerConnectionFactoryのインスタンスを作成
    val options = PeerConnectionFactory.Options()
    val factory = PeerConnectionFactory.builder()
        .setOptions(options)
        .createPeerConnectionFactory()
}