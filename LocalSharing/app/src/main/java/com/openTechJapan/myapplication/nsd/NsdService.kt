package com.openTechJapan.myapplication.nsd

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import timber.log.Timber
import java.net.InetAddress

class NsdService {
    lateinit var nsdManager: NsdManager
    private val TAG = this::class.java.name
    private val serviceNameBase = "local_sharing"
    private var mServiceName = ""
    private var mService: NsdServiceInfo? = null
    // YOU NEED TO CALL IT WHEN onCreate is called
    fun registerService(port: Int, context: Context) {
        val serviceInfo = NsdServiceInfo().apply {
            // The name is subject to change based on conflicts
            // with other services advertised on the same network.
            serviceName = "local_sharing"
            serviceType = "_nsdchat._tcp"
            this.port = port
        }
        nsdManager = (context.getSystemService(Context.NSD_SERVICE) as NsdManager).apply {
            registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, registrationListener)
        }
    }

    private val registrationListener = object : NsdManager.RegistrationListener {

        override fun onServiceRegistered(info: NsdServiceInfo) {
            // Save the service name. Android may have changed it in order to
            // resolve a conflict, so update the name you initially requested
            // with the name Android actually used.
            mServiceName = info.serviceName
        }

        override fun onRegistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
            // Registration failed! Put debugging code here to determine why.
        }

        override fun onServiceUnregistered(arg0: NsdServiceInfo) {
            // Service has been unregistered. This only happens when you call
            // NsdManager.unregisterService() and pass in this listener.
        }

        override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
            // Unregistration failed. Put debugging code here to determine why.
        }
    }

    // Instantiate a new DiscoveryListener
    private val discoveryListener = object : NsdManager.DiscoveryListener {

        // Called as soon as service discovery begins.
        override fun onDiscoveryStarted(regType: String) {
            Timber.tag(TAG).d("Service discovery started")
        }

        override fun onServiceFound(service: NsdServiceInfo?) {
            // A service was found! Do something with it.
            Timber.tag(TAG).d( "Service discovery success$service")
            Timber.d(TAG, "Unknown Service Type: ${service?.serviceType}")

            service?.let {
                if (it.serviceName.contains("NsdChat"))
                    nsdManager.resolveService(service, resolveListener)
            }

        }

        override fun onServiceLost(service: NsdServiceInfo?) {
            // When the network service is no longer available.
            // Internal bookkeeping code goes here.
            Timber.e(TAG, "service lost: $service")
        }

        override fun onDiscoveryStopped(serviceType: String) {
            Timber.i(TAG, "Discovery stopped: $serviceType")
        }

        override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
            Timber.e(TAG, "Discovery failed: Error code:$errorCode")
            nsdManager.stopServiceDiscovery(this)
        }

        override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
            Timber.e(TAG, "Discovery failed: Error code:$errorCode")
            nsdManager.stopServiceDiscovery(this)
        }
    }

    private val resolveListener = object : NsdManager.ResolveListener {

        override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
            // Called when the resolve fails. Use the error code to debug.
            Timber.tag(TAG).e( "Resolve failed: $errorCode")
        }

        override fun onServiceResolved(serviceInfo: NsdServiceInfo) {
            Timber.tag(TAG).e(TAG, "Resolve Succeeded. $serviceInfo")

            if (serviceInfo.serviceName == mServiceName) {
                Timber.tag(TAG).d(TAG, "Same IP.")
                return
            }
            mService = serviceInfo
            val port: Int = serviceInfo.port
            val host: InetAddress = serviceInfo.host
        }
    }
}